package antifraud.domain.repository;

import antifraud.domain.entity.Ip;
import antifraud.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpRep extends CrudRepository<Ip, Long> {
    Optional<Ip> findIpByIpIgnoreCase(String ip);
}