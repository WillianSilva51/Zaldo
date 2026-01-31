package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.CreateWalletUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.DeleteWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.FindWalletByUserIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.UpdateWalletUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.CreateWalletRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.UpdateWalletRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.PaginatedResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.WalletResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.WalletMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/wallets")
@Validated
@RequiredArgsConstructor
public class WalletController {
    private final CreateWalletUseCase createWalletUseCase;
    private final FindWalletByUserIdUseCase findWalletByUserIdUseCase;
    private final DeleteWalletByIdUseCase deleteWalletByIdUseCase;
    private final UpdateWalletUseCase updateWalletUseCase;
    private final WalletMapper walletMapper;

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody @Valid CreateWalletRequest request) {
        Wallet domainObject = walletMapper.toDomain(request);
        Wallet created = createWalletUseCase.execute(domainObject);
        return ResponseEntity.created(URI.create("/wallets/" + created.getId()))
                .body(walletMapper.toResponse(created));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PaginatedResponse<WalletResponse>> findWalletsByUserId(@PathVariable String userId,
                                                                                 @RequestParam(defaultValue = "0") @Min(value = 0, message = "Valor mínimo da página é 0") int page,
                                                                                 @RequestParam(defaultValue = "10") @Max(value = 100, message = "O valor máximo do tamanho é 100") @Min(value = 1, message = "Valor mínimo do tamanho é 1") int size,
                                                                                 @RequestParam(defaultValue = "createdAt") String sort,
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

    @PutMapping("/{id}")
    public ResponseEntity<WalletResponse> updateWallet(@PathVariable Long id, @RequestParam String userId, @RequestBody @Valid UpdateWalletRequest request) {
        Wallet domainObject = walletMapper.toDomainByUpdate(request);

        Wallet updated = updateWalletUseCase.execute(id, userId, domainObject);

        return ResponseEntity.ok(walletMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id, @RequestParam String userId) {
        deleteWalletByIdUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }
}
