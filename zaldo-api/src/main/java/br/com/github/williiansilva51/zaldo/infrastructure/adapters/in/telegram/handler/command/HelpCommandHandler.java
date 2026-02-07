package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class HelpCommandHandler implements TelegramCommandHandler {

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public SendMessage execute(String telegramId, Long chatId, String text, String username) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Help")
                .build();
    }
}
