package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.FindUserByTelegramIdUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command.TelegramCommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ZaldoTelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;
    private final FindUserByTelegramIdUseCase findUserByTelegramIdUseCase;
    private final Map<String, TelegramCommandHandler> commandHandlers;
    private final Map<String, TelegramCallbackHandler> callbackHandlers;

    public ZaldoTelegramBot(@Value("${api.security.token.bot}") String token,
                            FindUserByTelegramIdUseCase findUserByTelegramId,
                            List<TelegramCommandHandler> commandList,
                            List<TelegramCallbackHandler> callbackList) {
        botToken = token;
        telegramClient = new OkHttpTelegramClient(getBotToken());
        findUserByTelegramIdUseCase = findUserByTelegramId;
        commandHandlers = commandList.stream()
                .collect(Collectors.toMap(TelegramCommandHandler::getCommandName, Function.identity()));
        callbackHandlers = callbackList.stream()
                .collect(Collectors.toMap(TelegramCallbackHandler::getActionName, Function.identity()));
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(List<Update> updates) {
        LongPollingSingleThreadUpdateConsumer.super.consume(updates);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }

    private void handleMessage(Message message) {
        String text = message.getText();
        String telegramId = message.getFrom().getId().toString();
        Long chatId = message.getChatId();
        String userName = message.getFrom().getUserName();

        String command = text.split(" ")[0];

        TelegramCommandHandler handler = commandHandlers.get(command);

        if (handler != null) {

            SendMessage sendMessage = handler.execute(telegramId, chatId, text, userName);

            executeClient(sendMessage);
        } else {
            SendMessage sendMessage = commandHandlers.get("/help")
                    .execute(telegramId, chatId, text, userName);

            executeClient(sendMessage);
        }
    }

    private void executeClient(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeClient(BotApiMethod<?> method) {
        try {
            telegramClient.execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String action = callbackQuery.getData();

        TelegramCallbackHandler handler = callbackHandlers.get(action);

        if (handler != null) {
            User user = findUserByTelegramIdUseCase.execute(callbackQuery.getFrom().getId().toString())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

            BotApiMethod<?> response = handler.execute(callbackQuery, user);

            executeClient(response);
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}
