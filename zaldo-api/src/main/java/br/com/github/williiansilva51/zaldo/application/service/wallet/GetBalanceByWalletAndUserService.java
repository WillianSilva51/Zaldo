package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.GetBalanceByWalletAndUserUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetBalanceByWalletAndUserService implements GetBalanceByWalletAndUserUseCase {
    private final WalletRepositoryPort walletRepositoryPort;

    @Override
    public BigDecimal execute(Long walletId, String userId) {
        return walletRepositoryPort.getTotalBalanceByWalletAndUser(walletId, userId);
    }
}
