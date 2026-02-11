package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByUserIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.PaginatedResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.WalletResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.WalletMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/wallets")
@Validated
@RequiredArgsConstructor
@Tag(name = "User Wallets", description = "Endpoints to manage User Wallets")
public class UserWalletController {
    private final FindWalletByUserIdUseCase findWalletByUserIdUseCase;
    private final WalletMapper walletMapper;

    @GetMapping
    @Operation(summary = "Finds all wallets by user ID", description = "Returns a paginated list of wallets")
    @ApiResponse(responseCode = "200", description = "Wallets found successfully")
    public ResponseEntity<PaginatedResponse<WalletResponse>> findWalletsByUserId(@PathVariable String userId,
                                                                                 @RequestParam(defaultValue = "0") @Min(value = 0, message = "Valor mínimo da página é 0") int page,
                                                                                 @RequestParam(defaultValue = "10") @Max(value = 100, message = "O valor máximo do tamanho é 100") @Min(value = 1, message = "Valor mínimo do tamanho é 1") int size,
                                                                                 @RequestParam(defaultValue = "createdAt") WalletSortField sort,
                                                                                 @RequestParam(defaultValue = "DESC") DirectionOrder direction) {
        Paginated<Wallet> paginated = findWalletByUserIdUseCase.execute(userId, page, size, sort, direction);

        List<WalletResponse> wallets = paginated.content()
                .stream()
                .map(walletMapper::toResponse)
                .toList();

        PaginatedResponse<WalletResponse> response = new PaginatedResponse<>(
                wallets,
                paginated.totalElements(),
                paginated.totalPages(),
                paginated.currentPage());

        return ResponseEntity.ok(response);
    }

}
