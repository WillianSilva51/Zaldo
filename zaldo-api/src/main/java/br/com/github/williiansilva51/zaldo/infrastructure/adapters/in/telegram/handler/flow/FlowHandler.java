package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface FlowHandler {
    SendMessage handleInput(Long chatId, String text, User user);
}
