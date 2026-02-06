package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.TransactionSortField;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.FindTransactionByWalletIdUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.PaginatedResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.TransactionResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.TransactionMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets/{walletId}/transactions")
@Validated
@RequiredArgsConstructor
public class WalletTransactionController {
    private final FindTransactionByWalletIdUseCase findTransactionByWalletIdUseCase;
    private final TransactionMapper transactionMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<TransactionResponse>> findTransactionsByWalletId(@PathVariable Long walletId,
                                                                                             @RequestParam(defaultValue = "0") @Min(value = 0, message = "Valor mínimo da página é 0") int page,
                                                                                             @RequestParam(defaultValue = "10") @Max(value = 100, message = "O valor máximo do tamanho é 100") @Min(value = 1, message = "Valor mínimo do tamanho é 1") int size,
                                                                                             @RequestParam(defaultValue = "date") TransactionSortField sort,
                                                                                             @RequestParam(defaultValue = "DESC") DirectionOrder direction) {
        Paginated<Transaction> paginated = findTransactionByWalletIdUseCase.execute(walletId, page, size, sort, direction);

        List<TransactionResponse> transactions = paginated.content()
                .stream()
                .map(transactionMapper::toResponse)
                .toList();

        PaginatedResponse<TransactionResponse> response = new PaginatedResponse<>(
                transactions,
                paginated.totalElements(),
                paginated.totalPages(),
                paginated.currentPage());

        return ResponseEntity.ok(response);
    }

}
