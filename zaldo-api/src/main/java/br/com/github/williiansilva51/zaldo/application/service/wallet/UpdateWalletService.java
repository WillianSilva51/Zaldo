package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.UpdateWalletUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateWalletService implements UpdateWalletUseCase {
    private final FindWalletByIdService findWalletByIdService;
    private final WalletRepositoryPort walletRepositoryPort;

    @Override
    @Transactional
    public Wallet execute(Long id, String authenticatedUserId, Wallet wallet) {
        Wallet existingWallet = findWalletByIdService.execute(id);

        if (!existingWallet.getUser().getId().equals(authenticatedUserId)) {
            throw new DomainValidationException("Você não tem permissão para atualizar esta carteira.");
        }

        existingWallet.update(wallet);

        return walletRepositoryPort.save(existingWallet);
    }
}
