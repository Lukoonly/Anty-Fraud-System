package antifraud.api.controller;

import antifraud.api.dto.*;
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
public class SuspiciousIpController {

    SuspiciousIpService antiFraudService;
    StolenCardService stolenCardService;
    IpMapper ipMapper;
    StolenCardMapper stolenCardMapper;

    @PostMapping("/suspicious-ip")
    public IpResponseDTO addIpAddress(@Valid @RequestBody  IpDTO ipDTO) {
        return ipMapper.toIpResponseDTOFromIp(antiFraudService.addIp(ipDTO));
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public SuccessStatusResponseDTO deleteIp(@PathVariable String ip) {
        antiFraudService.deleteId(ip);
        return new SuccessStatusResponseDTO(String.format("IP %s successfully removed!", ip));
    }

    @GetMapping("/suspicious-ip")
    public List<IpResponseDTO> getAllIp() {
        return antiFraudService.getAllIp().stream()
                .map(ip -> ipMapper.toIpResponseDTOFromIp(ip))
                .collect(Collectors.toList());
    }
}