package antifraud.api.mapper;

import antifraud.api.dto.TransactionDTO;
import antifraud.domain.entity.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    public Card toCardFromTransactionDTO(TransactionDTO transactionDTO) {
        return Card.builder()
                .number(transactionDTO.getNumber())
                .maxManual(1500)
                .maxAllowed(200)
                .build();
    }
}