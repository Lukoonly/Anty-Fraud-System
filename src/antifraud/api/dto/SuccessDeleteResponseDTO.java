package antifraud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class SuccessDeleteResponseDTO {
    String username;
    String status;
}
