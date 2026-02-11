package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.CreateWalletUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.DeleteWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.UpdateWalletUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.CreateWalletRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.UpdateWalletRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.WalletResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.WalletMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/wallets")
@Validated
@RequiredArgsConstructor
@Tag(name = "Wallets", description = "Endpoints to manage Wallets")
public class WalletController {
    private final CreateWalletUseCase createWalletUseCase;
    private final FindWalletByIdUseCase findWalletByIdUseCase;
    private final DeleteWalletByIdUseCase deleteWalletByIdUseCase;
    private final UpdateWalletUseCase updateWalletUseCase;
    private final WalletMapper walletMapper;

    @PostMapping
    @Operation(summary = "Creates a new wallet", description = "Returns the created wallet")
    @ApiResponse(responseCode = "201", description = "Wallet created successfully")
    public ResponseEntity<WalletResponse> createWallet(@RequestBody @Valid CreateWalletRequest request) {
        Wallet domainObject = walletMapper.toDomain(request);
        Wallet created = createWalletUseCase.execute(domainObject);
        return ResponseEntity.created(URI.create("/wallets/" + created.getId()))
                .body(walletMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds a wallet by id", description = "Returns the wallet found")
    @ApiResponse(responseCode = "200", description = "Wallet found successfully")
    public ResponseEntity<WalletResponse> getWalletById(@PathVariable Long id) {
        Wallet wallet = findWalletByIdUseCase.execute(id);
        return ResponseEntity.ok(walletMapper.toResponse(wallet));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a wallet by id", description = "Returns the updated wallet")
    @ApiResponse(responseCode = "200", description = "Wallet updated successfully")
    public ResponseEntity<WalletResponse> updateWallet(@PathVariable Long id, @RequestParam String userId, @RequestBody @Valid UpdateWalletRequest request) {
        Wallet domainObject = walletMapper.toDomainByUpdate(request);

        Wallet updated = updateWalletUseCase.execute(id, userId, domainObject);

        return ResponseEntity.ok(walletMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a wallet by id", description = "Returns no content")
    @ApiResponse(responseCode = "204", description = "Wallet deleted successfully")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id, @RequestParam String userId) {
        deleteWalletByIdUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }
}
