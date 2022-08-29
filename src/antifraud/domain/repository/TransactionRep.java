package antifraud.domain.repository;

import antifraud.domain.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRep extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByNumberAndIpAndDateBetween(
            String number,
            String ip,
            LocalDateTime publicationDateStart,
            LocalDateTime publicationDateEnd
    );

    List<Transaction> findAllByDateBetween(LocalDateTime publicationDateStart,
                                           LocalDateTime publicationDateEnd);

    Optional<Transaction> findTransactionById(long id);

    List<Transaction> findAllByNumber(String number);
}
