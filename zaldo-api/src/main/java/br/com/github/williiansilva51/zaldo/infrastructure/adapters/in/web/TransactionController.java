package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.*;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction.CreateTransactionRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction.UpdateTransactionRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.PaginatedResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.TransactionResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.TransactionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Validated
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
    public ResponseEntity<PaginatedResponse<TransactionResponse>> getAllTransactions(@RequestParam(required = false) TransactionType type,
                                                                                     @RequestParam(required = false) LocalDate date,
                                                                                     @RequestParam(defaultValue = "0") @Min(value = 0, message = "Valor mínimo da página é 0") int page,
                                                                                     @RequestParam(defaultValue = "10") @Max(value = 100, message = "O valor máximo do tamanho é 100") @Min(value = 1, message = "Valor mínimo do tamanho é 1") int size,
                                                                                     @RequestParam(defaultValue = "date") String sort,
                                                                                     @RequestParam(defaultValue = "DESC") DirectionOrder direction) {
        Paginated<Transaction> paginated = listTransactionUseCase.execute(type, date, page, size, sort, direction);

        List<TransactionResponse> dtoList = paginated.content()
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        PaginatedResponse<TransactionResponse> responseList = new PaginatedResponse<>(
                dtoList,
                paginated.totalElements(),
                paginated.totalPages(),
                paginated.currentPage()
        );

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
        Transaction newInfo = transactionMapper.toDomainByUpdate(request);

        Transaction updated = updateTransactionUseCase.execute(id, newInfo);

        return ResponseEntity.ok(transactionMapper.toResponse(updated));
    }
}
