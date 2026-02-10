package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.GetBalanceByWalletAndUserUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SelectWalletCallbackHandler implements TelegramCallbackHandler {
    private final UserSessionManager sessionManager;
    private final FindWalletByIdUseCase findWalletByIdUseCase;
    private final GetBalanceByWalletAndUserUseCase getBalanceByWalletAndUserUseCase;

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

        context.setChatState(ChatState.IDLE);
        context.setTempWalletId(walletId);
        context.setTempWalletName(wallet.getName());
        sessionManager.save(chatId, context);

        BigDecimal balance = getBalanceByWalletAndUserUseCase.execute(context.getTempWalletId(), user.getId());

        String text = String.format(
                "🏦 <b>Carteira: %s</b>\n💰 Saldo: R$ %.2f\n\nO que deseja fazer?",
                wallet.getName(), balance
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
