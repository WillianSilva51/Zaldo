package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramCommandHandler {
    String getCommandName();

    SendMessage execute(String telegramId, Long chatId, String text, String username);
}
