package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class EditTransactionFieldCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;

    @Override
    public String getActionName() {
        return BotAction.EDIT_TRANSACTION_FIELD.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();

        String fieldToEdit = BotAction.extractArg(data);

        FlowContext context = sessionManager.get(chatId);

        context.setChatState(ChatState.WAITING_EDIT_TX_VALUE);
        context.setEditingField(fieldToEdit);
        sessionManager.save(chatId, context);

        String promptMessage = switch (fieldToEdit) {
            case "AMOUNT" -> "💰 Digite o <b>NOVO VALOR</b> (Ex: 50.50):";
            case "DESCRIPTION" -> "📝 Digite a <b>NOVA DESCRIÇÃO</b>:";
            case "DATE" -> "📅 Digite a <b>NOVA DATA</b> (DD/MM/AAAA):";
            case "TYPE" ->
                    "📂 Digite o <b>NOVO TIPO</b> (Digite 'RECEITA' ou 'DESPESA'):"; // Pode ser melhorado com botões no futuro
            default -> "✏️ Digite o novo valor:";
        };

        return SendMessage.builder()
                .chatId(chatId)
                .text(promptMessage)
                .parseMode("HTML")
                .build();
    }
}
