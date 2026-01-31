package br.com.github.williiansilva51.zaldo.core.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;

import java.util.Optional;

public interface WalletRepositoryPort {
    Wallet save(Wallet wallet);

    Optional<Wallet> findById(Long id);

    Paginated<Wallet> findByUserId(String userId, int page, int size, WalletSortField sort, DirectionOrder direction);

    void deleteById(Long id);
}
