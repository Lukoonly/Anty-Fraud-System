package antifraud.api.controller;

import antifraud.api.dto.TransactionDTO;
import antifraud.api.dto.TransactionResponseDTO;
import antifraud.api.dto.TransactionStatusResponseDTO;
import antifraud.api.mapper.TransactionMapper;
import antifraud.service.ControlService;
import antifraud.service.TransactionService;
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
public class TransactionController {

    TransactionMapper transactionMapper;
    ControlService controlService;
    TransactionService transactionService;

    @PostMapping("/transaction")
    public TransactionStatusResponseDTO addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return transactionMapper.toTransactionStatusResponseDTOrFromTransactionStatus(transactionService.addTransaction(transactionDTO));
    }

    @GetMapping("/history")
    public List<TransactionResponseDTO> getTransactionHistory() {
        return transactionService.getTransactionHistory().stream()
                .map(transaction -> transactionMapper.toTransactionResponseDTOFromTransaction(transaction))
                .collect(Collectors.toList());
    }
}
