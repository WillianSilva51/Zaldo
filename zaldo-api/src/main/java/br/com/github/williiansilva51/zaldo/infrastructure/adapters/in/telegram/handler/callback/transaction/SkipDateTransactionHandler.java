package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow.CreateTransactionFlowHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SkipDateTransactionHandler implements TelegramCallbackHandler {
    private final CreateTransactionFlowHandler createTransactionFlowHandler;

    @Override
    public String getActionName() {
        return "BTN_SKIP_DATE";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        return createTransactionFlowHandler.handleInput(callbackQuery.getMessage().getChatId(), null, user.getId());
    }
}
