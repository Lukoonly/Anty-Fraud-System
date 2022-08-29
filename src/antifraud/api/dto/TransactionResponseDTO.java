package antifraud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class TransactionResponseDTO {
    long transactionId;
    long amount;
    String ip;
    String number;
    String region;
    LocalDateTime date;
    String result;
    String feedback;
}
