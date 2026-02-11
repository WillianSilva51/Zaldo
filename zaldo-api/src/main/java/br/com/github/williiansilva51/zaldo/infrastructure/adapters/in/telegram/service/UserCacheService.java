package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.service;

import br.com.github.williiansilva51.zaldo.application.ports.in.user.FindUserByTelegramIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCacheService {
    private final UserSessionManager sessionManager;
    private final FindUserByTelegramIdUseCase findUserByTelegramIdUseCase;

    public User getAuthenticatedUser(String telegramId, Long chatId) {
        FlowContext context = sessionManager.get(chatId);

        if (context.getAuthenticatedUser() != null) {
            return context.getAuthenticatedUser();
        }

        log.info("Buscando usuário: {} no Banco de Dados para cacheamento...", telegramId);
        Optional<User> userOptional = findUserByTelegramIdUseCase.execute(telegramId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            context.setAuthenticatedUser(user);
            sessionManager.save(chatId, context);

            return user;
        }

        return null;
    }
}
