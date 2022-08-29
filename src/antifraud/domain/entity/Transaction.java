package antifraud.domain.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long amount;
    private String ip;
    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    private String number;
    private String region;
    private LocalDateTime date;
    private String result;
    private String feedback;
}