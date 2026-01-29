package br.com.github.williiansilva51.zaldo.core.ports.in.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;

public interface CreateWalletUseCase {
    Wallet execute(Wallet wallet);
}
