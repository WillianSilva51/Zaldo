package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ManegeUserCallbackHandler implements TelegramCallbackHandler {
    @Override
    public String getActionName() {
        return BotAction.MANEGE_USER.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        String informationUser = String.format(
                """
                        👤 <b>Seus dados</b>
                        
                        🆔 <b>ID:</b> <code>%s</code>
                        📛 <b>Nome:</b> %s
                        📧 <b>Email:</b> %s
                        🔐 <b>Senha:</b> ***********
                        🤖 <b>Telegram ID:</b> <code>%s</code>
                        
                        Você pode editar suas informações ou excluir sua conta abaixo 👇""",
                safe(user.getId()),
                safe(user.getName()),
                safe(user.getEmail()),
                safe(user.getTelegramId())
        );

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(informationUser)
                .replyMarkup(MenuUtils.createManegeUserKeyboard())
                .parseMode("HTML")
                .build();
    }

    private String safe(String value) {
        return (value == null || value.isBlank()) ? "—" : value;
    }
}
