package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface TelegramCommandHandler {
    String getCommandName();

    SendMessage execute(Message message, String username);
}
