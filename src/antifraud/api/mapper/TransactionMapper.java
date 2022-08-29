package antifraud.api.mapper;

import antifraud.api.dto.TransactionDTO;
import antifraud.api.dto.TransactionResponseDTO;
import antifraud.api.dto.TransactionStatusResponseDTO;
import antifraud.domain.entity.Card;
import antifraud.domain.entity.Transaction;
import antifraud.domain.entity.component.TransactionStatus;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionStatusResponseDTO toTransactionStatusResponseDTOrFromTransactionStatus(TransactionStatus transactionStatus) {
        return TransactionStatusResponseDTO.builder()
                .result(transactionStatus.getStatus())
                .info(transactionStatus.getInfo())
                .build();
    }

    public Transaction toTransactionFromTransactionDTO(TransactionDTO transactionDTO, String result, Card card) {
        return Transaction.builder()
                .amount(transactionDTO.getAmount())
                .date(transactionDTO.getDate())
                .ip(transactionDTO.getIp())
                .number(transactionDTO.getNumber())
                .region(transactionDTO.getRegion())
                .feedback("")
                .result(result)
                .card(card)
                .build();
    }

    public TransactionResponseDTO toTransactionResponseDTOFromTransaction(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .result(transaction.getResult())
                .amount(transaction.getAmount())
                .transactionId(transaction.getId())
                .feedback(transaction.getFeedback())
                .date(transaction.getDate())
                .region(transaction.getRegion())
                .number(transaction.getNumber())
                .ip(transaction.getIp())
                .build();
    }
}