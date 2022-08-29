package antifraud.api.controller;

import antifraud.api.dto.TransactionFeedbackDTO;
import antifraud.api.dto.TransactionResponseDTO;
import antifraud.api.mapper.TransactionMapper;
import antifraud.service.FeedbackService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/antifraud")
@AllArgsConstructor
@NoArgsConstructor
@RestController
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;
    @Autowired
    TransactionMapper transactionMapper;

    @PutMapping("/transaction")
    public TransactionResponseDTO addFeedback(@RequestBody TransactionFeedbackDTO transactionFeedbackDTO) {
        return transactionMapper
                .toTransactionResponseDTOFromTransaction(feedbackService.addFeedback(transactionFeedbackDTO));
    }

    @GetMapping("/history/{number}")
    public List<TransactionResponseDTO> getTransactionByNumber(@PathVariable String number) {
        return feedbackService.getTransactionByNumber(number).stream()
                .map(transaction -> transactionMapper.toTransactionResponseDTOFromTransaction(transaction))
                .collect(Collectors.toList());
    }
}
