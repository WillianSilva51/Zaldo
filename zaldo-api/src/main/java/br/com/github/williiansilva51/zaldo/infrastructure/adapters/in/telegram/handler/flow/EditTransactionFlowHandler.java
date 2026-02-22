package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.UpdateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.exceptions.BusinessRuleException;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EditTransactionFlowHandler implements FlowHandler {
    private final UserSessionManager sessionManager;
    private final UpdateTransactionUseCase updateTransactionUseCase;

    @Override
    public boolean canHandle(ChatState chatState) {
        return chatState.equals(ChatState.WAITING_EDIT_TX_VALUE);
    }

    @Override
    public SendMessage handleInput(Long chatId, String text, String userId) {
        FlowContext context = sessionManager.get(chatId);

        Long transactionId = context.getTempTransactionId();
        String fieldBeingEdited = context.getEditingField();

        if (transactionId == null || fieldBeingEdited == null) {
            resetContext(context, chatId);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("❌ Erro de contexto. Tente novamente.")
                    .replyMarkup(MenuUtils.createMainKeyboard())
                    .build();
        }

        try {
            Transaction transaction = getNewTransaction(text, fieldBeingEdited);

            updateTransactionUseCase.execute(transactionId, transaction);

            resetContext(context, chatId);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("✅ Transação atualizada com sucesso!")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.SELECT_TRANSACTION.build(transactionId)))).build())
                    .build();
        } catch (IllegalArgumentException | BusinessRuleException e) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("❌ " + e.getMessage() + ".")
                    .build();
        } catch (Exception e) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("❌ Valor inválido. Verifique o formato e tente novamente.\nErro: " + e.getMessage())
                    .build();
        }
    }

    private @NonNull Transaction getNewTransaction(String text, String fieldBeingEdited) {
        Transaction transaction = new Transaction();

        switch (fieldBeingEdited) {
            case "AMOUNT" -> transaction.setAmount(new BigDecimal(text.replace(",", ".")));
            case "DESCRIPTION" -> transaction.setDescription(text);
            case "DATE" -> transaction.setDate(LocalDate.parse(text, MenuUtils.getFormat()));
            case "TYPE" -> {
                if (text.equalsIgnoreCase("receita")) transaction.setType(TransactionType.INCOME);
                else if (text.equalsIgnoreCase("despesa")) transaction.setType(TransactionType.EXPENSE);
                else throw new IllegalArgumentException("Tipo inválido");
            }
            default -> throw new IllegalArgumentException("Campo sendo editado desconhecido: " + fieldBeingEdited);
        }
        return transaction;
    }

    private void resetContext(FlowContext context, Long chatId) {
        context.setChatState(ChatState.IDLE);
        context.setEditingField(null);
        sessionManager.save(chatId, context);
    }
}
