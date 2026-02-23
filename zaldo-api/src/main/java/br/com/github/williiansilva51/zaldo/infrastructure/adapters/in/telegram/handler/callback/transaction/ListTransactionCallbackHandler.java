package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.FindTransactionByWalletIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.TransactionSortField;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.ChatState;
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
public class ListTransactionCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final FindTransactionByWalletIdUseCase findTransactionByWalletIdUseCase;

    @Override
    public String getActionName() {
        return BotAction.LIST_TRANSACTIONS.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        String actionPage = BotAction.extractArg(data);

        sessionManager.setChatState(chatId, ChatState.IDLE);

        int page;
        try {
            page = Integer.parseInt(actionPage);
        } catch (NumberFormatException e) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Algo deu errado. Tente novamente.");
        }

        FlowContext context = sessionManager.get(chatId);

        if (context == null) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Algo deu errado. Tente novamente.");
        }

        Long walletId = context.getTempWalletId();

        if (walletId == null) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Selecione uma carteira primeiro.");
        }

        Paginated<Transaction> transactionPaginated = findTransactionByWalletIdUseCase
                .execute(context.getTempWalletId(), page, 10, TransactionSortField.description, DirectionOrder.DESC);

        if (transactionPaginated.totalElements() == 0) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId).text("\uD83D\uDCED <b>Extrato da Carteira</b>\n\nNão há transações registradas para essa carteira.")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.SELECT_WALLET.build(walletId))))
                            .build())
                    .parseMode("HTML")
                    .build();
        }

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("""
                        💰 <b>Extrato da Carteira</b>
                        
                        Confira abaixo suas movimentações \uD83D\uDC47""")
                .parseMode("HTML")
                .replyMarkup(MenuUtils.createListTransactions(transactionPaginated))
                .build();
    }
}
