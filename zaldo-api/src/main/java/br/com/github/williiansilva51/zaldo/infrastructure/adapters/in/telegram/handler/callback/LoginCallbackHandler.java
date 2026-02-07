package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class LoginCallbackHandler implements TelegramCallbackHandler {
    @Override
    public String getActionName() {
        return "login";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        return SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("Ótimo! Digite o <b>e-mail</b> que você deseja usar para o acesso Web:")
                .parseMode("HTML")
                .build();
    }
}
