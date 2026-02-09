package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class CreateTransactionFlowHandler implements FlowHandler {
    private final UserSessionManager sessionManager;
    private final CreateTransactionUseCase createTransactionUseCase;
    private final Validator validator;

    @Override
    public boolean canHandle(ChatState chatState) {
        return chatState.name().startsWith("WAITING_TRANSACTION");
    }

    @Override
    public SendMessage handleInput(Long chatId, String text, String userId) {
        FlowContext context = sessionManager.get(chatId);

        if (context.getChatState().equals(ChatState.WAITING_TRANSACTION_DESCRIPTION)) {
            return null;
        } else if (context.getChatState().equals(ChatState.WAITING_TRANSACTION_AMOUNT)) {
            return null;
        } else if (context.getChatState().equals(ChatState.WAITING_TRANSACTION_DATE)) {
            return null;
        }

        return null;
    }
}
