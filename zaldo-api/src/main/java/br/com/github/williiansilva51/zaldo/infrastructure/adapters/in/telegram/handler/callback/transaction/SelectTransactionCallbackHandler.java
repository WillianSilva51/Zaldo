package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.FindTransactionByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
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

@Component
@RequiredArgsConstructor
public class SelectTransactionCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final FindTransactionByIdUseCase findTransactionByIdUseCase;

    @Override
    public String getActionName() {
        return BotAction.SELECT_TRANSACTION.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        String transactionIdStr = BotAction.extractArg(data);
        long transactionId;

        try {
            transactionId = Long.parseLong(transactionIdStr);
        } catch (NumberFormatException ex) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Algo deu errado. Tente novamente.");
        }

        Transaction transaction = findTransactionByIdUseCase.execute(transactionId);

        FlowContext context = sessionManager.get(chatId);

        context.setChatState(ChatState.IDLE);
        context.setTempTransactionId(transactionId);
        context.setTempTransactionDescription(transaction.getDescription());

        sessionManager.save(chatId, context);

        String text = """
                <b>📄 Detalhes da Transação</b>
                
                💰 <b>Valor:</b> R$ %s
                📝 <b>Descrição:</b> %s
                📅 <b>Data:</b> %s
                📂 <b>Tipo:</b> %s
                
                O que deseja fazer?
                """.formatted(
                MenuUtils.numberFormat(transaction.getAmount()),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType() == TransactionType.INCOME ? "Receita" : "Despesa"
        );

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .parseMode("HTML")
                .replyMarkup(MenuUtils.createTransactionsKeyboard())
                .build();
    }
}
