package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface FlowHandler {
    boolean canHandle(ChatState chatState);

    SendMessage handleInput(Long chatId, String text, String userId);
}
