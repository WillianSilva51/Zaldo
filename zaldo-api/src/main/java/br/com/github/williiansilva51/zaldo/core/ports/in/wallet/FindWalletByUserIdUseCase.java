package br.com.github.williiansilva51.zaldo.core.ports.in.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;

public interface FindWalletByUserIdUseCase {
    Paginated<Wallet> execute(String userId, int page, int size, String sort, String direction);
}
