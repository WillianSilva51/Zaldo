package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.CreateWalletUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateWalletService implements CreateWalletUseCase {
    private final WalletRepositoryPort walletRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Wallet execute(Wallet wallet) {
        String userId = wallet.getUser().getId();

        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + userId));

        wallet.setUser(user);
        wallet.setCreatedAt(LocalDateTime.now());

        return walletRepositoryPort.save(wallet);
    }
}
