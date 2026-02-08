package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.CreateWalletUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.CacheUtils;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.CreateWalletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateWalletFlowHandler implements FlowHandler {
    private final UserSessionManager sessionManager;
    private final CreateWalletUseCase createWalletUseCase;
    private final CacheUtils cacheUtils;
    private final Validator validator;

    @Override
    public boolean canHandle(ChatState chatState) {
        return chatState.name().startsWith("WAITING_WALLET");
    }

    @Override
    public SendMessage handleInput(Long chatId, String text, String userId) {
        FlowContext context = sessionManager.get(chatId);

        if (context.getChatState().equals(ChatState.WAITING_WALLET_NAME)) {
            return processWalletNameInput(chatId, text, context);
        } else if (context.getChatState().equals(ChatState.WAITING_WALLET_DESCRIPTION)) {
            return processWalletDescriptionInput(chatId, text, context, userId);
        }

        return null;
    }

    private SendMessage processWalletNameInput(Long chatId, String text, FlowContext context) {
        Set<ConstraintViolation<CreateWalletRequest>> violations = validator
                .validateValue(CreateWalletRequest.class, "name", text);

        if (!violations.isEmpty()) {
            String msgError = violations.iterator().next().getMessage();

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("❌ " + msgError + ". Tente novamente:")
                    .build();
        }

        context.setChatState(ChatState.WAITING_WALLET_DESCRIPTION);
        context.setTempWalletName(text);

        sessionManager.save(chatId, context);

        return SendMessage.builder()
                .chatId(chatId)
                .text("📧 Nome salvo! Agora digite a descrição da <b>carteira</b> (Opcional):")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("BTN_CREATE_WALLET"))).build())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createButton("Pular descrição", "BTN_SKIP_DESCRIPTION"))).build())
                .parseMode("HTML")
                .build();
    }

    private SendMessage processWalletDescriptionInput(Long chatId, String text, FlowContext context, String userId) {
        User user = cacheUtils.getAuthenticatedUser(userId, chatId);

        Wallet wallet = Wallet.builder()
                .name(context.getTempWalletName())
                .description(text)
                .user(user)
                .build();

        createWalletUseCase.execute(wallet);

        sessionManager.clearSession(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("✅ <b>Sucesso!</b> Carteira criada.\n\nO que deseja fazer agora?")
                .parseMode("HTML")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("BTN_LIST_WALLETS"))).build())
                .build();
    }
}
