package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface TelegramCallbackHandler {
    String getActionName();

    BotApiMethod<?> execute(CallbackQuery callbackQuery, User user);
}
