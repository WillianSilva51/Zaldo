package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.DeleteTransactionByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeleteTransactionCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final DeleteTransactionByIdUseCase deleteTransactionByIdUseCase;

    @Override
    public String getActionName() {
        return BotAction.DELETE_TRANSACTION.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        FlowContext context = sessionManager.get(chatId);

        Long walletId = context.getTempWalletId();
        Long transactionId = context.getTempTransactionId();

        if (transactionId == null || walletId == null) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("⚠\uFE0F Erro: Não foi possível identificar a transação ou a carteira. Tente listar novamente ou a transação.")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.LIST_WALLETS.build(0))))
                            .build())
                    .build();
        }

        try {
            deleteTransactionByIdUseCase.execute(transactionId, user.getId());
            sessionManager.clearSession(chatId);

            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(("✅ <b>Transação apagada com sucesso!</b>"))
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.SELECT_WALLET.build(walletId)))).build())
                    .parseMode("HTML")
                    .build();

        } catch (Exception e) {
            log.error("Erro ao apagar a transação", e);

            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("❌ Ocorreu um erro ao tentar apagar a transação")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.LIST_TRANSACTIONS.build((walletId)))))
                            .build())
                    .build();
        }
    }
}
