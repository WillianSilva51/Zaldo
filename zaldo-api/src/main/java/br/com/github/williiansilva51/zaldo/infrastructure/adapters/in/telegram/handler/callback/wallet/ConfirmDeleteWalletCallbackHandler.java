package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.wallet;

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
public class ConfirmDeleteWalletCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;

    @Override
    public String getActionName() {
        return BotAction.CONFIRM_DELETE_WALLET.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        FlowContext context = sessionManager.get(chatId);
        String walletName = context.getTempWalletName();

        if (walletName == null) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Sessão expirada, tente novamente.");
        }

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Tem certeza de que deseja excluir a carteira \"" + walletName + "\"? Essa ação não pode ser desfeita.")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createButton("\uD83D\uDDD1\uFE0F Excluir carteira", BotAction.DELETE_WALLET.getActionName())))
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.SELECT_WALLET.build(context.getTempWalletId()))))
                        .build())
                .build();
    }
}
