package br.com.github.williiansilva51.zaldo.core.ports.in.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;

public interface FindWalletByUserIdUseCase {
    Paginated<Wallet> execute(String userId, int page, int size, WalletSortField sort, DirectionOrder direction);
}
