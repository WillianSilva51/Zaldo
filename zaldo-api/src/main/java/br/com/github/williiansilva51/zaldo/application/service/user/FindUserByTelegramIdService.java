package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.application.ports.in.user.FindUserByTelegramIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class FindUserByTelegramIdService implements FindUserByTelegramIdUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Optional<User> execute(String telegramId) {
        return userRepositoryPort.findByTelegramId(telegramId);
    }
}
