package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
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
public class ConfirmDeleteTransactionCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;

    @Override
    public String getActionName() {
        return BotAction.CONFIRM_DELETE_TRANSACTION.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        FlowContext context = sessionManager.get(chatId);
        Long transactionId = context.getTempTransactionId();
        String transactionDescription = context.getTempTransactionDescription();

        if (transactionDescription == null || transactionId == null) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Sessão expirada, tente novamente.");
        }

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Tem certeza de que deseja excluir a Transação \"" + transactionDescription + "\"? Essa ação não pode ser desfeita.")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createButton("\uD83D\uDDD1\uFE0F Excluir Transação", BotAction.DELETE_TRANSACTION.getActionName())))
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.SELECT_TRANSACTION.build(transactionId))))
                        .build())
                .build();
    }
}
