package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.FindUserByTelegramIdUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command.TelegramCommandHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow.FlowRouter;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ZaldoTelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;
    private final FindUserByTelegramIdUseCase findUserByTelegramIdUseCase;
    private final Map<String, TelegramCommandHandler> commandHandlers;
    private final Map<String, TelegramCallbackHandler> callbackHandlers;
    private final FlowRouter flowRouter;
    private final UserSessionManager sessionManager;

    public ZaldoTelegramBot(@Value("${api.security.token.bot}") String token,
                            FindUserByTelegramIdUseCase findUserByTelegramId,
                            List<TelegramCommandHandler> commandList,
                            List<TelegramCallbackHandler> callbackList,
                            FlowRouter router,
                            UserSessionManager manager) {
        botToken = token;
        telegramClient = new OkHttpTelegramClient(getBotToken());
        findUserByTelegramIdUseCase = findUserByTelegramId;
        commandHandlers = commandList.stream()
                .collect(Collectors.toMap(TelegramCommandHandler::getCommandName, Function.identity()));
        callbackHandlers = callbackList.stream()
                .collect(Collectors.toMap(TelegramCallbackHandler::getActionName, Function.identity()));
        flowRouter = router;
        sessionManager = manager;
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

    private void executeClient(BotApiMethod<?> method) {
        try {
            telegramClient.execute(method);
        } catch (TelegramApiException e) {
            log.error("Erro ao executar método na API do Telegram: {}", e.getMessage(), e);
        }
    }

    private void handleMessage(Message message) {
        String text = message.getText();
        String telegramId = message.getFrom().getId().toString();
        Long chatId = message.getChatId();
        String userName = message.getFrom().getUserName();

        FlowContext context = sessionManager.get(chatId);
        String userId = authenticateUser(telegramId, context, chatId);

        if (userId != null && context.getChatState() != ChatState.IDLE) {
            SendMessage flowResponse = flowRouter.route(context.getChatState(), chatId, text, userId);

            if (flowResponse != null) {
                executeClient(flowResponse);
                return;
            }
        }

        String command = text.split(" ")[0];

        TelegramCommandHandler handler = commandHandlers.get(command);

        SendMessage sendMessage;

        if (handler != null) {
            sendMessage = handler.execute(message, userName);
        } else {
            sendMessage = commandHandlers.get("/help")
                    .execute(message, userName);
        }

        executeClient(sendMessage);
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String actionRaw = callbackQuery.getData();

        String actionKey = actionRaw.contains(":") ? actionRaw.split(":")[0] : actionRaw;

        TelegramCallbackHandler handler = callbackHandlers.get(actionKey);

        if (handler != null) {
            User user = findUserByTelegramIdUseCase.execute(callbackQuery.getFrom().getId().toString())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

            BotApiMethod<?> response = handler.execute(callbackQuery, user);

            executeClient(response);
        } else {
            SendMessage sendMessage = commandHandlers.get("/help")
                    .execute(Message.builder().chat(callbackQuery.getMessage().getChat()).build(),
                            callbackQuery.getFrom().getUserName());

            executeClient(sendMessage);
        }
    }

    private @Nullable String authenticateUser(String telegramId, FlowContext context, Long chatId) {
        String userId = context.getAuthenticatedUserId();

        if (userId == null) {
            Optional<User> userOptional = findUserByTelegramIdUseCase.execute(telegramId);

            if (userOptional.isPresent()) {
                userId = userOptional.get().getId();
                context.setAuthenticatedUserId(userId);

                sessionManager.save(chatId, context);
            }
        }
        return userId;
    }
}
