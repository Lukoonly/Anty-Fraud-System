package antifraud.api.mapper;

import antifraud.api.dto.ActivityUpdateDTO;
import antifraud.api.dto.ResultDTO;
import antifraud.api.dto.SuccessStatusResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {
    public ResultDTO toResultDTO(String result) {
        return ResultDTO.builder()
                .result(result)
                .build();
    }

    public SuccessStatusResponseDTO toSuccessStatusResponseDTO(ActivityUpdateDTO activityUpdateDTO) {
        return SuccessStatusResponseDTO.builder().status(String.format("User %s %s!",
                        activityUpdateDTO.getUsername().toLowerCase(), activityUpdateDTO.getOperation().toLowerCase() + "ed"))
                .build();
    }
}
