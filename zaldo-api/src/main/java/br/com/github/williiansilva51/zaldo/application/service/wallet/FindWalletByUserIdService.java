package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.FindWalletByUserIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindWalletByUserIdService implements FindWalletByUserIdUseCase {
    private final WalletRepositoryPort walletRepositoryPort;

    @Override
    public Paginated<Wallet> execute(String userId, int page, int size, String sort, String direction) {
        return walletRepositoryPort.findByUserId(userId, page, size, sort, direction);
    }
}
