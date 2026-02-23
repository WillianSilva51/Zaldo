package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.user;

import br.com.github.williiansilva51.zaldo.application.ports.in.user.DeleteUserByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class DeleteUserCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final DeleteUserByIdUseCase deleteUserByIdUseCase;

    @Override
    public String getActionName() {
        return BotAction.DELETE_USER.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        try {
            deleteUserByIdUseCase.execute(user.getId());
            sessionManager.clearSession(chatId);
        } catch (ResourceNotFoundException e) {
            return MenuUtils.createErrorMessage(chatId, messageId, e.getMessage());
        }

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Todos seus dados foram apagados.\n\nMuito obrigado por usar o Zaldo, até a próxima!!! \uD83D\uDE0A")
                .build();
    }
}
