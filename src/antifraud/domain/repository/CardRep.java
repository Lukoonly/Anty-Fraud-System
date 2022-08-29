package antifraud.domain.repository;

import antifraud.domain.entity.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRep extends CrudRepository<Card, Long> {
    Optional<Card> findCardByNumber(String number);
}