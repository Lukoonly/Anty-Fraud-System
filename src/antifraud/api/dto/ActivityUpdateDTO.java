package antifraud.api.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class ActivityUpdateDTO {
    @NotEmpty
    @NotNull
    @NotBlank
    String username;
    @NotEmpty
    @NotNull
    @NotBlank
    String operation;
}
