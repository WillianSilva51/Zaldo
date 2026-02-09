package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
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
public class CreateTransactionCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;

    @Override
    public String getActionName() {
        return "BTN_NEW_TRANSACTION";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        String type = data.split(":")[1];
        TransactionType transactionType = TransactionType.valueOf(type);

        FlowContext context = sessionManager.get(chatId);
        context.setChatState(ChatState.WAITING_TRANSACTION_DESCRIPTION);
        context.setTempTransactionType(transactionType);

        Long walletId = context.getTempWalletId();

        if (walletId == null) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Selecione uma carteira primeiro.");
        }

        sessionManager.save(chatId, context);

        String text = transactionType == TransactionType.EXPENSE
                ? "\uD83D\uDCB8 <b>Nova despesa</b>\n\n" +
                "Digite a descrição da despesa.\n\n" +
                "<i>Exemplo:</i> Mercado, Aluguel, Uber"
                : "\uD83D\uDCB0 <b>Nova receita</b>\n\n" +
                "Digite a descrição da receita.\n\n" +
                "<i>Exemplo:</i> Salário, Freelance, Reembolso";


        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("SEL_WALLET:" + walletId)))
                        .build())
                .parseMode("HTML")
                .build();
    }
}
