package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.FindTransactionByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
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
public class EditTransactionCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final FindTransactionByIdUseCase findTransactionByIdUseCase;

    @Override
    public String getActionName() {
        return BotAction.EDIT_TRANSACTION.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        FlowContext context = sessionManager.get(chatId);

        Long currentTransactionId = context.getTempTransactionId();
        assert currentTransactionId != null;

        Transaction transaction = findTransactionByIdUseCase.execute(currentTransactionId);

        String text = """
                <b>📄 Detalhes da Transação</b>
                
                💰 <b>Valor:</b> %s
                📝 <b>Descrição:</b> %s
                📅 <b>Data:</b> %s
                📂 <b>Tipo:</b> %s
                
                O que deseja alterar?
                """.formatted(
                MenuUtils.numberFormat(transaction.getAmount()),
                transaction.getDescription(),
                transaction.getDate().format(MenuUtils.getFormat()),
                transaction.getType() == TransactionType.INCOME ? "Receita" : "Despesa"
        );

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(
                        MenuUtils.createButton("💰 Valor", BotAction.EDIT_TRANSACTION_FIELD.build("AMOUNT")),
                        MenuUtils.createButton("📝 Descrição", BotAction.EDIT_TRANSACTION_FIELD.build("DESCRIPTION"))
                ))
                .keyboardRow(new InlineKeyboardRow(
                        MenuUtils.createButton("\uD83D\uDCC5 Data", BotAction.EDIT_TRANSACTION_FIELD.build("DATE")),
                        MenuUtils.createButton("📂 Tipo", BotAction.EDIT_TRANSACTION_FIELD.build("TYPE"))
                ))
                .keyboardRow(new InlineKeyboardRow(
                        MenuUtils.createBackButton(BotAction.SELECT_TRANSACTION.build(currentTransactionId))
                ))
                .build();

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(keyboard)
                .parseMode("HTML")
                .build();
    }
}
