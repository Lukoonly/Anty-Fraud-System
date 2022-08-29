package antifraud.api.mapper;

import antifraud.api.dto.IpDTO;
import antifraud.api.dto.IpResponseDTO;
import antifraud.domain.entity.Ip;
import org.springframework.stereotype.Component;

@Component
public class IpMapper {

    public Ip toIpFromIpDTO(IpDTO ipDTO) {
        return Ip.builder()
                .ip(ipDTO.getIp())
                .build();
    }

    public IpResponseDTO toIpResponseDTOFromIp(Ip ip) {
        return IpResponseDTO.builder()
                .id(ip.getId())
                .ip(ip.getIp())
                .build();
    }
}
