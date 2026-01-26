package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.*;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.CreateTransactionRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.UpdateTransactionRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.TransactionResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.TransactionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final CreateTransactionUseCase createTransactionUseCase;
    private final ListTransactionUseCase listTransactionUseCase;
    private final FindTransactionByIdUseCase findTransactionByIdUseCase;
    private final DeleteTransactionByIdUseCase deleteTransactionByIdUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody @Valid CreateTransactionRequest request) {
        Transaction domainObject = transactionMapper.toDomain(request);

        Transaction transaction = createTransactionUseCase.execute(domainObject);

        TransactionResponse created = transactionMapper.toResponse(transaction);

        return ResponseEntity.created(URI.create("/transactions/" + created.id())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@RequestParam(required = false) TransactionType type,
                                                                        @RequestParam(required = false) LocalDate date) {
        List<TransactionResponse> responseList = listTransactionUseCase
                .execute(type, date)
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        Transaction transaction = findTransactionByIdUseCase.execute(id);

        return ResponseEntity.ok(transactionMapper.toResponse(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable Long id) {
        deleteTransactionByIdUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody @Valid UpdateTransactionRequest request) {
        Transaction newInfo = transactionMapper.toDomain(request);

        Transaction updated = updateTransactionUseCase.execute(id, newInfo);

        return ResponseEntity.ok(transactionMapper.toResponse(updated));
    }
}
