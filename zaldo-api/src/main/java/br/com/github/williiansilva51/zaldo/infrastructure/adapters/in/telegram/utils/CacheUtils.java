package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.FindUserByTelegramIdUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CacheUtils {
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
