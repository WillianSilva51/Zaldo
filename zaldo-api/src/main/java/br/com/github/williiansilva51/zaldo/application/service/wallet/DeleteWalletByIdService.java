package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.DeleteWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteWalletByIdService implements DeleteWalletByIdUseCase {
    private final WalletRepositoryPort walletRepositoryPort;
    private final FindWalletByIdUseCase findWalletByIdUseCase;

    @Override
    public void execute(Long id, String authenticatedUserId) {
        Wallet wallet = findWalletByIdUseCase.execute(id);

        if (!wallet.getUser().getId().equals(authenticatedUserId)) {
            throw new DomainValidationException("Você não tem permissão para deletar esta carteira.");
        }

        walletRepositoryPort.deleteById(id);
    }
}
