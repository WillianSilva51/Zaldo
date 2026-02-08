package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class MainMenuCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;

    @Override
    public String getActionName() {
        return "BTN_MAIN_MENU";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        FlowContext context = sessionManager.get(chatId);

        if (context.getChatState() != ChatState.IDLE) {
            context.setChatState(ChatState.IDLE);
        }

        sessionManager.save(chatId, context);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("🏠 <b>Menu Principal</b>\n\nEscolha uma opção:")
                .parseMode("HTML")
                .replyMarkup(MenuUtils.createMainKeyboard())
                .build();
    }
}
