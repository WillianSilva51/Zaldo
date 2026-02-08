package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command.LoginCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@RequiredArgsConstructor
public class LoginCallbackHandler implements TelegramCallbackHandler {
    private final LoginCommandHandler loginCommandHandler;

    @Override
    public String getActionName() {
        return "BTN_LOGIN";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        return loginCommandHandler.execute(Message.builder().chat(callbackQuery.getMessage().getChat()).build(),
                callbackQuery.getFrom().getUserName());
    }
}
