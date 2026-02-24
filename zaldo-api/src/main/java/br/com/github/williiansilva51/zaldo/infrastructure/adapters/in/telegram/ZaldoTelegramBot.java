package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command.TelegramCommandHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow.FlowRouter;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.service.UserCacheService;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ZaldoTelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String botToken;
    private final UserCacheService userCacheService;
    private final Map<String, TelegramCommandHandler> commandHandlers;
    private final Map<String, TelegramCallbackHandler> callbackHandlers;
    private final FlowRouter flowRouter;
    private final UserSessionManager sessionManager;

    public ZaldoTelegramBot(@Value("${api.security.token.bot}") String token,
                            TelegramClient tClient,
                            UserCacheService cache,
                            List<TelegramCommandHandler> commandList,
                            List<TelegramCallbackHandler> callbackList,
                            FlowRouter router,
                            UserSessionManager manager) {
        botToken = token;
        telegramClient = tClient;
        userCacheService = cache;
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

    private void executeClient(BotApiMethod<?> method, Long chatId) {
        try {
            var response = telegramClient.execute(method);

            boolean isMessage = chatId != null && method instanceof SendMessage sendMessage && sendMessage.getReplyMarkup() != null;

            if (isMessage) {
                if (response instanceof Message sentMessage) {
                    FlowContext context = sessionManager.get(chatId);
                    context.setLastMessageId(sentMessage.getMessageId());
                    sessionManager.save(chatId, context);
                }
            }
        } catch (TelegramApiException e) {
            log.error("Erro ao executar método na API do Telegram: {}", e.getMessage(), e);
        }
    }

    private void removeKeyboard(Long chatId, Integer messageId) {
        if (messageId == null) {
            return;
        }
        try {
            telegramClient.execute(EditMessageReplyMarkup.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .replyMarkup(null)
                    .build());
        } catch (TelegramApiException e) {
            log.warn("Teclado da msg {} já removido ou expirado.", messageId);
        }
    }

    private void clearActiveKeyboard(Long chatId, FlowContext context) {
        if (context.getLastMessageId() != null) {
            removeKeyboard(chatId, context.getLastMessageId());
            context.setLastMessageId(null);
            sessionManager.save(chatId, context);
        }
    }

    private void handleMessage(Message message) {
        String text = message.getText();
        String telegramId = message.getFrom().getId().toString();
        Long chatId = message.getChatId();
        String userName = null;

        FlowContext context = sessionManager.get(chatId);

        clearActiveKeyboard(chatId, context);

        User user = userCacheService.getAuthenticatedUser(telegramId, chatId);

        if (user != null && context.getChatState() != ChatState.IDLE) {
            SendMessage flowResponse = flowRouter.route(context.getChatState(), chatId, text, user.getId());

            if (flowResponse != null) {
                executeClient(flowResponse, chatId);
                return;
            }
        }

        String command = text.split(" ")[0];

        TelegramCommandHandler handler = commandHandlers.get(command);

        BotApiMethod<?> sendMessage;

        if (handler != null) {
            if (user != null) {
                userName = user.getName();
            }
            sendMessage = handler.execute(message, userName);
        } else {
            if (user == null) {
                executeClient(SendMessage.builder()
                        .chatId(chatId)
                        .text("❌ Você não tem conta no Zaldo use o comando /start para criar sua conta!!")
                        .build(), null);

                return;
            }
            sendMessage = commandHandlers.get("/help")
                    .execute(message, userName);
        }

        executeClient(sendMessage, chatId);
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer callbackMessageId = callbackQuery.getMessage().getMessageId();
        String telegramId = callbackQuery.getFrom().getId().toString();
        String actionRaw = callbackQuery.getData();
        String actionKey = actionRaw.contains(":") ? actionRaw.split(":")[0] : actionRaw;

        FlowContext context = sessionManager.get(chatId);

        if (context.getLastMessageId() != null && !context.getLastMessageId().equals(callbackMessageId)) {
            try {
                telegramClient.execute(AnswerCallbackQuery.builder()
                        .callbackQueryId(callbackQuery.getId())
                        .showAlert(true)
                        .text("⏳ Esta ação expirou. Use os botões mais recentes.")
                        .build());
            } catch (TelegramApiException e) {
                log.debug("Falha ao responder callback query expirada. chatId={}, messageId={}, callbackQueryId={}",
                        chatId, callbackMessageId, callbackQuery.getId(), e);
            }
            removeKeyboard(chatId, callbackMessageId);
            return;
        }

        try {
            telegramClient.execute(
                    AnswerCallbackQuery.builder()
                            .callbackQueryId(callbackQuery.getId())
                            .build());
        } catch (TelegramApiException e) {
            log.error("Erro ao responder callback query", e);
        }

        User user = userCacheService.getAuthenticatedUser(telegramId, chatId);

        if (user == null) {
            return;
        }

        TelegramCallbackHandler handler = callbackHandlers.get(actionKey);

        if (handler != null) {
            BotApiMethod<?> response = handler.execute(callbackQuery, user);
            executeClient(response, chatId);
        } else {
            BotApiMethod<?> sendMessage = commandHandlers.get("/help")
                    .execute(Message.builder().chat(callbackQuery.getMessage().getChat()).build(),
                            callbackQuery.getFrom().getUserName());

            executeClient(sendMessage, chatId);
        }
    }
}
