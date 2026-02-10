package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.FindWalletByUserIdUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.callback.TelegramCallbackHandler;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ListWalletCallbackHandler implements TelegramCallbackHandler {
    private final FindWalletByUserIdUseCase findWalletByUserIdUseCase;
    private final UserSessionManager sessionManager;

    @Override
    public String getActionName() {
        return "BTN_LIST_WALLETS";
    }

    @Override
    public BotApiMethod<?> execute(CallbackQuery callbackQuery, User user) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String data = callbackQuery.getData();

        sessionManager.setChatState(chatId, ChatState.IDLE);

        String actionPage = data.split(":")[1];

        int page;

        try {
            page = Integer.parseInt(actionPage);
        } catch (NumberFormatException e) {
            return MenuUtils.createErrorMessage(chatId, messageId, "Algo deu errado. Tente novamente.");
        }

        Paginated<Wallet> walletPaginated = findWalletByUserIdUseCase
                .execute(user.getId(), page, 5, WalletSortField.createdAt, DirectionOrder.DESC);

        if (walletPaginated.totalElements() == 0) {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("Você ainda não tem carteiras. Crie uma para começar!")
                    .replyMarkup(MenuUtils.createListWallets(walletPaginated))
                    .build();
        }

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Selecione uma carteira para gerenciar:")
                .replyMarkup(MenuUtils.createListWallets(walletPaginated))
                .build();
    }
}
