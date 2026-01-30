package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.CreateWalletUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.CreateWalletRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.WalletResponse;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper.WalletMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final CreateWalletUseCase createWalletUseCase;
    private final WalletMapper walletMapper;

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestBody @Valid CreateWalletRequest request) {
        Wallet domainObject = walletMapper.toDomain(request);
        Wallet created = createWalletUseCase.execute(domainObject);
        return ResponseEntity.created(URI.create("/wallets/" + created.getId()))
                .body(walletMapper.toResponse(created));
    }
}
