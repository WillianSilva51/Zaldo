package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@RequiredArgsConstructor
public class LoginCommandHandler implements TelegramCommandHandler {
    private final UserSessionManager userSessionManager;

    @Override
    public String getCommandName() {
        return "/login";
    }

    @Override
    public SendMessage execute(Message message, String username) {
        Long chatId = message.getChatId();

        userSessionManager.setChatState(chatId, ChatState.WAITING_LOGIN_EMAIL);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Ótimo! Digite o <b>e-mail</b> que você deseja usar para o acesso Web:")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("BTN_MAIN_MENU"))).build())
                .parseMode("HTML")
                .build();
    }
}
