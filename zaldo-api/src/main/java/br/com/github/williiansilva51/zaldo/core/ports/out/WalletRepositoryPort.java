package br.com.github.williiansilva51.zaldo.core.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;

public interface WalletRepositoryPort {
    Wallet save(Wallet wallet);

    Wallet findById(Long id);

    Paginated<Wallet> findByUserId(String userId, int page, int size, String sort, DirectionOrder direction);
}
