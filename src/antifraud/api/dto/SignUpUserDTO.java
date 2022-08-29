package antifraud.api.dto;


import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class SignUpUserDTO {
    @NotEmpty
    @NotNull
    String name;
    @NotEmpty
    @NotNull
    String username;
    @NotEmpty
    @NotNull
    String password;
}
