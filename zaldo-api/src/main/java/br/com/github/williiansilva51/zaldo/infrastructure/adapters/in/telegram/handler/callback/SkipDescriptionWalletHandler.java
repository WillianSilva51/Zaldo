package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow.CreateWalletFlowHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SkipDescriptionWalletHandler implements TelegramCallbackHandler {
    private final CreateWalletFlowHandler createWalletFlowHandler;

    @Override
    public String getActionName() {
        return "BTN_SKIP_DESCRIPTION";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        return createWalletFlowHandler.handleInput(callbackQuery.getMessage().getChatId(), null, user.getId());
    }
}
