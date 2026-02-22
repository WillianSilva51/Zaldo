package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.GetBalanceByWalletAndUserUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
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
        return BotAction.SELECT_WALLET.getActionName();
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        String walletIdStr = BotAction.extractArg(data);

        Long walletId;

        try {
            walletId = Long.parseLong(walletIdStr);
        } catch (NumberFormatException ex) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Algo deu errado. Tente novamente.");
        }

        Wallet wallet = findWalletByIdUseCase.execute(walletId);

        FlowContext context = sessionManager.get(chatId);

        context.setChatState(ChatState.IDLE);
        context.setTempWalletId(walletId);
        context.setTempWalletName(wallet.getName());
        sessionManager.save(chatId, context);

        String description = wallet.getDescription() == null ? "Sem descrição" : wallet.getDescription();
        BigDecimal balance = getBalanceByWalletAndUserUseCase.execute(context.getTempWalletId(), user.getId());

        String text = String.format(
                """
                        🏦 <b>Carteira: %s</b>
                        📄 Descrição: %s
                        💰 Saldo: %s
                        
                        O que deseja fazer?""",
                wallet.getName(), description, MenuUtils.numberFormat(balance)
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
