package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class SelectWalletHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final FindWalletByIdUseCase findWalletByIdUseCase;

    @Override
    public String getActionName() {
        return "SEL_WALLET";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        String walletIdStr = data.split(":")[1];
        Long walletId = Long.parseLong(walletIdStr);

        Wallet wallet = findWalletByIdUseCase.execute(walletId);

        FlowContext context = sessionManager.get(chatId);

        context.setTempWalletId(walletId);
        context.setTempWalletName(wallet.getName());
        sessionManager.save(chatId, context);

        String text = String.format(
                "🏦 <b>Carteira: %s</b>\n💰 Saldo: R$ ?\n\nO que deseja fazer?",
                wallet.getName()
        );

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(MenuUtils.createWalletsKeyboard())
                .parseMode("HTML")
                .build();
    }
}
