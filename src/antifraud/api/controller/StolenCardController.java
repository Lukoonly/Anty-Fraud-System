package antifraud.api.controller;

import antifraud.api.dto.StolenCardDTO;
import antifraud.api.dto.StolenCardResponseDTO;
import antifraud.api.dto.SuccessStatusResponseDTO;
import antifraud.api.mapper.IpMapper;
import antifraud.api.mapper.StolenCardMapper;
import antifraud.service.SuspiciousIpService;
import antifraud.service.StolenCardService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/antifraud")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@RestController
public class StolenCardController {

    SuspiciousIpService antiFraudService;
    StolenCardService stolenCardService;
    IpMapper ipMapper;
    StolenCardMapper stolenCardMapper;

    @PostMapping("/stolencard")
    public StolenCardResponseDTO addStolenCard(@Valid @RequestBody StolenCardDTO stolenCardDTO) {
        return stolenCardMapper.toStolenCardResponseDTOFromStolenCard(stolenCardService.addStolenCard(stolenCardDTO));
    }

    @DeleteMapping("/stolencard/{number}")
    public SuccessStatusResponseDTO deleteStolenCard(@PathVariable String number) {
        stolenCardService.deleteStolenCardByNumber(number);
        return new SuccessStatusResponseDTO(String.format("Card %s successfully removed!", number));
    }

    @GetMapping("/stolencard")
    public List<StolenCardResponseDTO> getAllStolenCard() {
        return stolenCardService.getAllStolenCard().stream()
                .map(stolenCard -> stolenCardMapper.toStolenCardResponseDTOFromStolenCard(stolenCard))
                .collect(Collectors.toList());
    }
}