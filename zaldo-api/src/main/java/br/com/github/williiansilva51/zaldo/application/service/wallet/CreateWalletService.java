package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.CreateWalletUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateWalletService implements CreateWalletUseCase {
    private final WalletRepositoryPort walletRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public Wallet execute(Wallet wallet) {
        String userId = wallet.getUser().getId();

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + userId));

        wallet.setUser(user);
        wallet.setCreatedAt(LocalDateTime.now());

        return walletRepositoryPort.save(wallet);
    }
}
