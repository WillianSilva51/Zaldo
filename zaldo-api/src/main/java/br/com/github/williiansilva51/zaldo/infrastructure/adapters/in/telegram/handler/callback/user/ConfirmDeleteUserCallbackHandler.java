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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@RequiredArgsConstructor
public class ConfirmDeleteUserCallbackHandler implements TelegramCallbackHandler {

    @Override
    public String getActionName() {
        return BotAction.CONFIRM_DELETE_USER.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("⚠\uFE0F Tem certeza de que deseja excluir seu usuário? Essa ação não pode ser desfeita.")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createButton("\uD83D\uDDD1\uFE0F Excluir usuário", BotAction.DELETE_USER.getActionName())))
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.CONFIG.getActionName())))
                        .build())
                .build();
    }
}
