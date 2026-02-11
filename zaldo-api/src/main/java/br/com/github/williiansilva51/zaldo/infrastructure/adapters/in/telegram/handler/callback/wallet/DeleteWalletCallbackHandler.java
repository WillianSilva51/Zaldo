package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.DeleteWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.User;
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
public class DeleteWalletCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final DeleteWalletByIdUseCase deleteWalletByIdUseCase;

    @Override
    public String getActionName() {
        return "BTN_DELETE_WALLET";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        FlowContext context = sessionManager.get(callbackQuery.getMessage().getChatId());

        Long walletId = context.getTempWalletId();

        if (walletId == null) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("⚠\uFE0F Erro: Não foi possível identificar a carteira. Tente listar novamente.")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("BTN_LIST_WALLETS:0")))
                            .build())
                    .build();
        }
        try {
            deleteWalletByIdUseCase.execute(walletId, context.getAuthenticatedUser().getId());

            sessionManager.clearSession(chatId);

            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(("✅ <b>Carteira apagada com sucesso!</b>\n\nTodas as transações vinculadas também foram removidas."))
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("BTN_LIST_WALLETS:0"))).build())
                    .parseMode("HTML")
                    .build();
        } catch (Exception e) {
            log.error("Erro ao apagar carteira", e);

            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("❌ Ocorreu um erro ao tentar apagar a carteira.")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("BTN_LIST_WALLETS:0")))
                            .build())
                    .build();
        }
    }
}
