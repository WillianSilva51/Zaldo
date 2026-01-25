package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.ports.in.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.ListTransactionUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.CreateTransactionRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.TransactionResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.TransactionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final CreateTransactionUseCase createTransactionUseCase;
    private final ListTransactionUseCase listTransactionUseCase;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid CreateTransactionRequest request) {
        Transaction domainObject = transactionMapper.toDomain(request);

        Transaction transaction = createTransactionUseCase.execute(domainObject);

        TransactionResponse created = transactionMapper.toResponse(transaction);

        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> responseList = listTransactionUseCase
                .execute()
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }
}
