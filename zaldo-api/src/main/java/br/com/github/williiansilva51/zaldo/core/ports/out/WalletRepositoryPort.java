package br.com.github.williiansilva51.zaldo.core.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;

public interface WalletRepositoryPort {
    Wallet save(Wallet wallet);
}
