package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FlowRouter {
    private final List<FlowHandler> flowHandlers;

    public SendMessage route(ChatState state, Long chatId, String text, String userId) {
        return flowHandlers.stream()
                .filter(flowHandler -> flowHandler.canHandle(state))
                .findFirst()
                .map(flowHandler -> flowHandler.handleInput(chatId, text, userId))
                .orElse(null);
    }
}
