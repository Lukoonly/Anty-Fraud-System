package antifraud.service;

import antifraud.api.dto.TransactionFeedbackDTO;
import antifraud.domain.entity.Card;
import antifraud.domain.entity.Transaction;
import antifraud.domain.entity.enumEntityes.TransactionsStatus;
import antifraud.domain.exceptions.BadRequestException;
import antifraud.domain.exceptions.ConflictDataException;
import antifraud.domain.exceptions.NotFoundException;
import antifraud.domain.exceptions.UnprocessableEntityException;
import antifraud.domain.repository.CardRep;
import antifraud.domain.repository.TransactionRep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Service
public class FeedbackService {

    private TransactionRep transactionRep;
    private CardRep cardRep;
    private StolenCardService stolenCardService;

    public List<Transaction> getTransactionByNumber(String number) {
        if (!stolenCardService.isValidCreditCardNumber(number)) {
            throw new BadRequestException("Wrong card format!");
        }
        List<Transaction> result = transactionRep.findAllByNumber(number);
        if (result.size() == 0) {
            throw new NotFoundException("Card is not exists");
        }
        return result;
    }

    public Transaction addFeedback(TransactionFeedbackDTO transactionFeedbackDTO) {
        Transaction transaction = transactionRep.findTransactionById(transactionFeedbackDTO.getTransactionId())
                .orElseThrow(() -> new NotFoundException("Transaction is not found!"));
        Card card = cardRep.findCardByNumber(transaction.getNumber()).get();
        if (isStatusExists(transactionFeedbackDTO.getFeedback())) {
            throw new BadRequestException("Is not valid feedback - " + transactionFeedbackDTO.getFeedback());
        }

        if (isFeedbackHasSimilarStatus(transactionFeedbackDTO.getFeedback(), transaction.getResult())) {
            throw new UnprocessableEntityException();
        }

        if (!transaction.getFeedback().isEmpty()) {
            throw new ConflictDataException();
        }

        updateTransactionLimits(transactionFeedbackDTO.getFeedback(),
                transaction.getResult(),
                card,
                (int) transaction.getAmount());

        cardRep.save(card);
        transaction.setFeedback(transactionFeedbackDTO.getFeedback());
        transaction.setCard(card);
        return transactionRep.save(transaction);
    }


    private void updateTransactionLimits(String feedback,
                                         String transactionStatus,
                                         Card card,
                                         int valueFromTransaction) {
        int currentTransactionAllowedLimit = card.getMaxAllowed();
        int currentTransactionManualLimit = card.getMaxManual();

        switch (feedback) {
            case "ALLOWED":
                updateAllowedTransactionLimits(transactionStatus,
                        card,
                        currentTransactionAllowedLimit,
                        currentTransactionManualLimit,
                        valueFromTransaction);
                break;
            case "MANUAL_PROCESSING":
                updateManualProcessingTransactionLimits(transactionStatus,
                        card,
                        currentTransactionAllowedLimit,
                        currentTransactionManualLimit,
                        valueFromTransaction);
                break;
            case "PROHIBITED":
                updateProhibitedTransactionLimits(transactionStatus,
                        card,
                        currentTransactionAllowedLimit,
                        currentTransactionManualLimit,
                        valueFromTransaction);
                break;
        }
    }

    private void updateAllowedTransactionLimits(String transactionStatus,
                                                Card card,
                                                int currentTransactionAllowedLimit,
                                                int currentTransactionManualLimit,
                                                int valueFromTransaction) {
        switch (transactionStatus) {
            case "MANUAL_PROCESSING":
                card.setMaxAllowed(getIncreasingTransactionLimit(currentTransactionAllowedLimit, valueFromTransaction));
                break;
            case "PROHIBITED":
                card.setMaxAllowed(getIncreasingTransactionLimit(currentTransactionAllowedLimit, valueFromTransaction));
                card.setMaxManual(getIncreasingTransactionLimit(currentTransactionManualLimit, valueFromTransaction));
                break;
        }
    }

    private void updateProhibitedTransactionLimits(String transactionStatus,
                                                   Card card,
                                                   int currentTransactionAllowedLimit,
                                                   int currentTransactionManualLimit,
                                                   int valueFromTransaction) {
        switch (transactionStatus) {
            case "ALLOWED":
                card.setMaxAllowed(getDecreasingTransactionLimit(currentTransactionAllowedLimit, valueFromTransaction));
                card.setMaxManual(getDecreasingTransactionLimit(currentTransactionManualLimit, valueFromTransaction));
                break;
            case "MANUAL_PROCESSING":
                card.setMaxManual(getDecreasingTransactionLimit(currentTransactionManualLimit, valueFromTransaction));
                break;
        }
    }

    private void updateManualProcessingTransactionLimits(String transactionStatus,
                                                         Card card,
                                                         int currentTransactionAllowedLimit,
                                                         int currentTransactionManualLimit,
                                                         int valueFromTransaction) {
        switch (transactionStatus) {
            case "ALLOWED":
                card.setMaxAllowed(getDecreasingTransactionLimit(currentTransactionAllowedLimit, valueFromTransaction));
                break;
            case "PROHIBITED":
                card.setMaxManual(getIncreasingTransactionLimit(currentTransactionManualLimit, valueFromTransaction));
                break;
        }
    }

    private int getIncreasingTransactionLimit(int currentLimit, int valueFromTransaction) {
        return (int) Math.ceil(0.8 * currentLimit + 0.2 * valueFromTransaction);
    }

    private int getDecreasingTransactionLimit(int currentLimit, int valueFromTransaction) {
        return (int) Math.ceil(0.8 * currentLimit - 0.2 * valueFromTransaction);
    }

    private boolean isStatusExists(String feedback) {
        return Arrays.stream(TransactionsStatus.values())
                .noneMatch(transactionsStatus -> feedback.equals(transactionsStatus.toString()));
    }

    private boolean isFeedbackHasSimilarStatus(String feedback, String transactionStatus) {
        return feedback.equals(transactionStatus);
    }
}
