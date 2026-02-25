package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.application.ports.in.user.UpdateUserUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@RequiredArgsConstructor
public class EditUsernameFlowHandler implements FlowHandler {
    private final UserSessionManager sessionManager;
    private final UpdateUserUseCase updateUserUseCase;

    @Override
    public boolean canHandle(ChatState chatState) {
        return chatState.equals(ChatState.WAITING_USERNAME);
    }

    @Override
    public SendMessage handleInput(Long chatId, String text, String userId) {
        FlowContext context = sessionManager.get(chatId);
        User user = context.getAuthenticatedUser();

        if (user != null) {
            user.update(User.builder().name(text).build());

            updateUserUseCase.execute(user.getId(), user);
        } else {
            context.setChatState(ChatState.IDLE);
            sessionManager.save(chatId, context);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Algo deu errado. Tente novamente.")
                    .replyMarkup(MenuUtils.createMainKeyboard())
                    .build();
        }

        sessionManager.clearSession(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("""
                        ✅ <b>Nome atualizado com sucesso!</b>
                        
                        Seu novo nome foi salvo.
                        Você já pode voltar para o gerenciamento da sua conta 👇
                        """)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton(BotAction.MANEGE_USER.getActionName()))).build())
                .parseMode("HTML")
                .build();
    }
}
