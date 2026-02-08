package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public class HelpCommandHandler implements TelegramCommandHandler {

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public SendMessage execute(Message message, String username) {
        Long chatId = message.getChatId();
        
        return SendMessage.builder()
                .chatId(chatId)
                .text("Help")
                .build();
    }
}
