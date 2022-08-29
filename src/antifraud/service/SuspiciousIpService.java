package antifraud.service;

import antifraud.api.dto.IpDTO;
import antifraud.api.mapper.IpMapper;
import antifraud.domain.entity.Ip;
import antifraud.domain.exceptions.BadRequestException;
import antifraud.domain.exceptions.ConflictDataException;
import antifraud.domain.exceptions.NotFoundException;
import antifraud.domain.repository.IpRep;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Service
public class SuspiciousIpService {

   private IpRep ipRep;
    private IpMapper ipMapper;

    public Ip addIp(IpDTO ipDTO) {
        String requestIp = ipDTO.getIp();
        if (isValidIp(requestIp)) {
            if (ipRep.findIpByIpIgnoreCase(requestIp).isPresent()) {
                throw new ConflictDataException("Ip is exists");
            }
            return ipRep.save(ipMapper.toIpFromIpDTO(ipDTO));
        } else {
            throw new BadRequestException("Not valid data!");
        }
    }

    public void deleteId(String ip) {
        if (isValidIp(ip)) {
            Ip ipEntity = ipRep.findIpByIpIgnoreCase(ip).orElseThrow(() -> new NotFoundException("Ip not found"));
            ipRep.delete(ipEntity);
        } else {
            throw new BadRequestException("Not valid data!");
        }
    }

    public List<Ip> getAllIp(){
        List<Ip> result = new ArrayList<>();
        ipRep.findAll().forEach(result::add);
        return result;
    }

    private boolean isValidIp(String ip) {
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";
        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;
        return Pattern.compile(regex).matcher(ip).matches();
    }
}
