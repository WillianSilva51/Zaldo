package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.UpdateWalletUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.BusinessRuleException;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateWalletService implements UpdateWalletUseCase {
    private final FindWalletByIdUseCase findWalletByIdUseCase;
    private final WalletRepositoryPort walletRepositoryPort;

    @Override
    public Wallet execute(Long id, String authenticatedUserId, Wallet wallet) {
        Wallet existingWallet = findWalletByIdUseCase.execute(id);

        if (!existingWallet.getUser().getId().equals(authenticatedUserId)) {
            throw new DomainValidationException("Você não tem permissão para atualizar esta carteira.");
        }

        if (!wallet.getName().equals(existingWallet.getName()) && walletRepositoryPort.existsByUserIdAndName(authenticatedUserId, wallet.getName())) {
            throw new BusinessRuleException(String.format(
                    "Já existe uma carteira com o nome '%s' para esse usuário.",
                    wallet.getName()
            ));
        }

        existingWallet.update(wallet);

        try {
            return walletRepositoryPort.save(existingWallet);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException(
                    "Já existe uma carteira com esse nome para esse usuário."
            );
        }
    }
}
