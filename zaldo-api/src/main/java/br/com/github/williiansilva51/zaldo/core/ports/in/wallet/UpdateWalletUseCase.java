package br.com.github.williiansilva51.zaldo.core.ports.in.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;

public interface UpdateWalletUseCase {
    Wallet execute(Long id, String authenticatedUserId, Wallet wallet);
}
