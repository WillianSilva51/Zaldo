package br.com.github.williiansilva51.zaldo.core.ports.in.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;

import java.util.List;

public interface ListWalletByIdUseCase {
    List<Wallet> execute();
}
