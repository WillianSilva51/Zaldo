package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.FindUserByTelegramIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class FindUserByTelegramIdService implements FindUserByTelegramIdUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Optional<User> execute(String telegramId) {
        return userRepositoryPort.findByTelegramId(telegramId);
    }
}
