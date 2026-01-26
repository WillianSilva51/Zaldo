package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.ports.in.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.ListTransactionUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.CreateTransactionRequest;
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
@RequestMapping("/transaction")
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

        return ResponseEntity.created(URI.create("/transaction/" + created.id())).body(created);
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
}
