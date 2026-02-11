package br.com.github.williiansilva51.zaldo.application.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletRepositoryPort {
    Wallet save(Wallet wallet);

    Optional<Wallet> findById(Long id);

    Paginated<Wallet> findByUserId(String userId, int page, int size, WalletSortField sort, DirectionOrder direction);

    void deleteById(Long id);

    BigDecimal getTotalBalanceByWalletAndUser(Long walletId, String userId);
}
