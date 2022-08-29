package antifraud.api.dto;

import lombok.Getter;

@Getter
public class TransactionFeedbackDTO {
    private long transactionId;
    private String feedback;
}