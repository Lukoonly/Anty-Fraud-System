package antifraud.domain.repository;

import antifraud.domain.entity.StolenCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StolenCardRep extends CrudRepository<StolenCard, Long> {
    Optional<StolenCard> findStolenCardByNumberIgnoreCase(String number);
}