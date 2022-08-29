package antifraud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ResponseSuccessRegDTO {
    long id;
    String name;
    String username;
    String role;
}
