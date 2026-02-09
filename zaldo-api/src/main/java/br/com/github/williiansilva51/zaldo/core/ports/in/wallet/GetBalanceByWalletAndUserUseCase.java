package br.com.github.williiansilva51.zaldo.core.ports.in.wallet;

import java.math.BigDecimal;

public interface GetBalanceByWalletAndUserUseCase {
    BigDecimal execute(Long walletId, String userId);
}
