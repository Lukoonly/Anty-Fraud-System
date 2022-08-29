package antifraud.api.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class IpDTO {
    @NotNull
    @NotEmpty
    String ip;
}
