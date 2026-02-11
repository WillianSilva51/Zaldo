package br.com.github.williiansilva51.zaldo.application.ports.in.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;

import java.util.Optional;

public interface FindUserByTelegramIdUseCase {
    Optional<User> execute(String telegramId);
}
