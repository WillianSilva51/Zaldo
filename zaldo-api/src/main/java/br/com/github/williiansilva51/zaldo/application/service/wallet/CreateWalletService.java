package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.CreateWalletUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.BusinessRuleException;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CreateWalletService implements CreateWalletUseCase {
    private final WalletRepositoryPort walletRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final int maximumNumberOfWallets;

    public CreateWalletService(WalletRepositoryPort walletRepositoryPort,
                               UserRepositoryPort userRepositoryPort,
                               @Value("${zaldo.wallet.limit:10}") int maximumNumberOfWallets) {
        this.walletRepositoryPort = walletRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.maximumNumberOfWallets = maximumNumberOfWallets;
    }

    @Override
    public Wallet execute(Wallet wallet) {
        String userId = wallet.getUser().getId();

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + userId));

        long currentWalletCount = walletRepositoryPort.countByUserId(userId);

        if (currentWalletCount >= maximumNumberOfWallets) {
            throw new BusinessRuleException(
                    String.format("Limite atingido. Você já possui %d/%d carteiras.", currentWalletCount, maximumNumberOfWallets));
        }

        if (walletRepositoryPort.existsByUserIdAndName(userId, wallet.getName())) {
            throw new BusinessRuleException(String.format(
                    "Já existe uma carteira com o nome '%s' para esse usuário.",
                    wallet.getName()
            ));
        }

        wallet.setUser(user);
        wallet.setCreatedAt(LocalDateTime.now());

        try {
            return walletRepositoryPort.save(wallet);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException(
                    "Já existe uma carteira com esse nome para esse usuário."
            );
        }
    }
}
