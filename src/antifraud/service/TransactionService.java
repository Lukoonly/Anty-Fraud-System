package antifraud.service;

import antifraud.api.dto.TransactionDTO;
import antifraud.api.mapper.CardMapper;
import antifraud.api.mapper.TransactionMapper;
import antifraud.domain.entity.Card;
import antifraud.domain.entity.Transaction;
import antifraud.domain.entity.component.TransactionStatus;
import antifraud.domain.exceptions.BadRequestException;
import antifraud.domain.repository.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;


@NoArgsConstructor
@Service
public class TransactionService {

    private StolenCardRep stolenCardRep;
    private IpRep ipRep;
    private TransactionMapper transactionMapper;
    private TransactionRep transactionRep;
    private CardRep cardRep;
    private CardMapper cardMapper;
    private boolean isProhibited;

    @Autowired
    public TransactionService(StolenCardRep stolenCardRep,
                              IpRep ipRep,
                              TransactionMapper transactionMapper,
                              TransactionRep transactionRep,
                              CardRep cardRep,
                              CardMapper cardMapper) {
        this.stolenCardRep = stolenCardRep;
        this.ipRep = ipRep;
        this.transactionMapper = transactionMapper;
        this.transactionRep = transactionRep;
        this.cardRep = cardRep;
        this.cardMapper = cardMapper;
    }

    public TransactionStatus addTransaction(TransactionDTO transactionDTO) {
        isProhibited = false;
        String status = "";
        List<Transaction> transactionsInTheLastHour = getListOfTransactionLastHour(transactionDTO);
        Set<String> infoList = new TreeSet<>();
        Card currentCard = getCard(transactionDTO);
        long amount = transactionDTO.getAmount();

        status = checkAmount(amount, infoList, currentCard);

        switch (status) {
            case "PROHIBITED":
                checkProhibitedStatus(infoList,
                        transactionsInTheLastHour,
                        transactionDTO.getNumber(),
                        transactionDTO.getIp(),
                        transactionDTO.getRegion());
                break;
            case "MANUAL_PROCESSING":
                if (checkProhibitedStatus(infoList,
                        transactionsInTheLastHour,
                        transactionDTO.getNumber(),
                        transactionDTO.getIp(),
                        transactionDTO.getRegion())) {
                    status = "PROHIBITED";
                    infoList.remove("amount");
                } else {
                    checkUniquesIpLastHourForManualProcessing(transactionsInTheLastHour, transactionDTO.getIp(), infoList);
                    checkUniquesRegionLastHourForManualProcessing(transactionsInTheLastHour, transactionDTO.getRegion()
                            , infoList);
                }
                break;
            default:
                checkProhibitedStatus(infoList, transactionsInTheLastHour, transactionDTO.getNumber(),
                        transactionDTO.getIp(), transactionDTO.getRegion());
                if (isProhibited) {
                    status = "PROHIBITED";
                    infoList.remove("amount");
                } else if (checkManualProcessingStatus(transactionsInTheLastHour, transactionDTO.getRegion()
                        , transactionDTO.getIp(), infoList)) {
                    status = "MANUAL_PROCESSING";
                }
        }

        String info = status.equals("ALLOWED") ? "none" : getInfo(infoList);
        cardRep.save(currentCard);
        transactionRep.save(transactionMapper.toTransactionFromTransactionDTO(transactionDTO, status, currentCard));
        return new TransactionStatus(status, info);
    }

    public List<Transaction> getTransactionHistory() {
        List<Transaction> result = new ArrayList<>();
        transactionRep.findAll().forEach(result::add);
        return result;
    }

    private Card getCard(TransactionDTO transactionDTO) {
        return cardRep.findCardByNumber(transactionDTO.getNumber())
                .orElse(cardMapper.toCardFromTransactionDTO(transactionDTO));
    }

    private boolean checkProhibitedStatus(Set<String> infoList,
                                          List<Transaction> transactions,
                                          String number,
                                          String ip,
                                          String region) {
        checkBlackListIpAndCard(infoList, number, ip);

        checkUniquesIpLastHourForProhibited(transactions, ip, infoList);
        checkUniquesRegionLastHourForProhibited(transactions, region, infoList);
        return infoList.size() > 1;
    }

    private boolean checkManualProcessingStatus(List<Transaction> transactions,
                                                String region, String ip, Set<String> info) {
        checkUniquesIpLastHourForManualProcessing(transactions, ip, info);
        checkUniquesRegionLastHourForManualProcessing(transactions, region, info);
        return info.size() > 0;
    }

    private void checkBlackListIpAndCard(Set<String> infoList, String number, String ip) {
        if (stolenCardRep.findStolenCardByNumberIgnoreCase(number).isPresent()) {
            infoList.add("card-number");
        }
        if (ipRep.findIpByIpIgnoreCase(ip).isPresent()) {
            infoList.add("ip");
        }
    }

    private String checkAmount(long amount, Set<String> infoList, Card card) {

        if (amount < 1) {
            throw new BadRequestException("Wrong amount");
        }
        String result = "";

        if (amount <= card.getMaxAllowed()) {
            result = "ALLOWED";
        } else if (amount <= card.getMaxManual()) {
            result = "MANUAL_PROCESSING";
            infoList.add("amount");
        } else {
            result = "PROHIBITED";
            infoList.add("amount");
        }
        return result;
    }

    private String getInfo(Set<String> info) {
        if (info.size() == 0) {
            return "amount";
        }

        StringBuilder result = new StringBuilder();
        for (String cur : info) {
            result.append(cur).append(", ");
        }
        result = new StringBuilder(result.substring(0, result.lastIndexOf(", ")));
        return result.toString();
    }

    public List<Transaction> getListOfTransactionLastHour(TransactionDTO transactionDTO) {
        return transactionRep.findAllByDateBetween(
                transactionDTO.getDate().minusHours(1)
                , transactionDTO.getDate());
    }

    private void checkUniquesRegionLastHourForProhibited(List<Transaction> transactions, String region, Set<String> info) {
        if (transactions.stream().map(Transaction::getRegion).collect(Collectors.toList()).stream()
                .filter(transaction -> !region.equals(transaction)).distinct()
                .count() > 2) {
            info.add("region-correlation");
            isProhibited = true;
        }
    }

    private void checkUniquesIpLastHourForProhibited(List<Transaction> transactions, String ip, Set<String> info) {
        if (transactions.stream().map(Transaction::getIp).collect(Collectors.toList()).stream()
                .filter(transaction -> !ip.equals(transaction)).distinct()
                .count() > 2) {
            info.add("ip-correlation");
            isProhibited = true;
        }
    }

    private void checkUniquesRegionLastHourForManualProcessing(List<Transaction> transactions, String region, Set<String> info) {
        if (transactions.stream().map(Transaction::getRegion).collect(Collectors.toList()).stream()
                .filter(transaction -> !region.equals(transaction)).distinct()
                .count() == 2) {
            info.add("region-correlation");
        }
    }

    private void checkUniquesIpLastHourForManualProcessing(List<Transaction> transactions, String ip, Set<String> info) {
        if (transactions.stream().map(Transaction::getIp).collect(Collectors.toList()).stream()
                .filter(transaction -> !ip.equals(transaction)).distinct()
                .count() == 2) {
            info.add("ip-correlation");
        }
    }
}