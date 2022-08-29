package antifraud.api.mapper;

import antifraud.api.dto.StolenCardDTO;
import antifraud.api.dto.StolenCardResponseDTO;
import antifraud.domain.entity.StolenCard;
import org.springframework.stereotype.Component;

@Component
public class StolenCardMapper {
    public StolenCard toStolenCardFromStolenCardDTO(StolenCardDTO stolenCardDTO) {
        return StolenCard.builder()
                .number(stolenCardDTO.getNumber())
                .build();
    }

    public StolenCardResponseDTO toStolenCardResponseDTOFromStolenCard(StolenCard stolenCard) {
        return StolenCardResponseDTO.builder()
                .id(stolenCard.getId())
                .number(stolenCard.getNumber())
                .build();
    }
}
