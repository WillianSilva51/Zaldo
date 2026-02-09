package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.GetBalanceByWalletAndUserUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
class GetBalanceByWalletAndUserService implements GetBalanceByWalletAndUserUseCase {
    private final WalletRepositoryPort walletRepositoryPort;

    @Override
    public BigDecimal execute(Long walletId, String userId) {
        return walletRepositoryPort.getTotalBalanceByWalletAndUser(walletId, userId);
    }
}
