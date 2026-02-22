package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByUserIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class FindWalletByUserIdService implements FindWalletByUserIdUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final WalletRepositoryPort walletRepositoryPort;

    @Override
    public Paginated<Wallet> execute(String userId, int page, int size, WalletSortField sort, DirectionOrder direction) {
        if (!userRepositoryPort.existsById(userId)) {
            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + userId);
        }

        return walletRepositoryPort.findByUserId(userId, page, size, sort, direction);
    }
}
