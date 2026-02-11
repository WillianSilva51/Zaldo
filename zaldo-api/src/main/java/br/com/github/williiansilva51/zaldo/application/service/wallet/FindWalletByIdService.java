package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindWalletByIdService implements FindWalletByIdUseCase {
    private final WalletRepositoryPort walletRepositoryPort;

    @Override
    public Wallet execute(Long id) {
        return walletRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada com ID: " + id));
    }
}
