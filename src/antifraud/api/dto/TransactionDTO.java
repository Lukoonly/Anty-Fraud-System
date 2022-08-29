package antifraud.api.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class TransactionDTO {
    long amount;
    @NotEmpty
    @NotNull
    String ip;
    @NotEmpty
    @NotNull
    String number;
    @NotEmpty
    @NotNull
    String region;
    LocalDateTime date;
}