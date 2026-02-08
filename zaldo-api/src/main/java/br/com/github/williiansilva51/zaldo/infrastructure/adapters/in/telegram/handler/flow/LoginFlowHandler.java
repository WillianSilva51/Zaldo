package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.UpdateUserUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.CacheUtils;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user.UpdateUserRequest;
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
public class LoginFlowHandler implements FlowHandler {
    private final UserSessionManager sessionManager;
    private final CacheUtils cacheUtils;
    private final UpdateUserUseCase updateUserUseCase;
    private final Validator validator;

    @Override
    public boolean canHandle(ChatState chatState) {
        return chatState.name().startsWith("WAITING_LOGIN");
    }

    @Override
    public SendMessage handleInput(Long chatId, String text, String userId) {
        FlowContext context = sessionManager.get(chatId);

        if (context.getChatState().equals(ChatState.WAITING_LOGIN_EMAIL)) {
            return processEmailInput(chatId, text, context);
        } else if (context.getChatState().equals(ChatState.WAITING_LOGIN_PASSWORD)) {
            return processPasswordInput(chatId, text, context, userId);
        }

        return null;
    }

    private SendMessage processEmailInput(Long chatId, String text, FlowContext context) {
        Set<ConstraintViolation<UpdateUserRequest>> violations =
                validator.validateValue(UpdateUserRequest.class, "email", text);

        if (!violations.isEmpty()) {
            String msgError = violations.iterator().next().getMessage();

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("❌ " + msgError + ". Tente novamente:")
                    .build();
        }

        context.setTempEmail(text);
        context.setChatState(ChatState.WAITING_LOGIN_PASSWORD);
        sessionManager.save(chatId, context);

        return SendMessage.builder()
                .chatId(chatId)
                .text("📧 E-mail salvo! Agora digite sua <b>senha</b>:")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("BTN_LOGIN"))).build())
                .parseMode("HTML")
                .build();
    }

    private SendMessage processPasswordInput(Long chatId, String text, FlowContext context, String userId) {
        User user = cacheUtils.getAuthenticatedUser(userId, chatId);

        String email = context.getTempEmail();

        // TODO: Encriptar aqui (BCrypt)

        user.update(User.builder()
                .email(email)
                .password(text)
                .build());

        updateUserUseCase.execute(user.getId(), user);

        sessionManager.clearSession(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("✅ <b>Sucesso!</b> Login configurado.\n\nO que deseja fazer agora?")
                .parseMode("HTML")
                .replyMarkup(MenuUtils.createMainKeyboard())
                .build();
    }
}
