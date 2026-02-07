package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.CreateUserUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.FindUserByTelegramIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StartCommandHandler implements TelegramCommandHandler {
    private final FindUserByTelegramIdUseCase findUserByTelegramIdUseCase;
    private final CreateUserUseCase createUserUseCase;

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public SendMessage execute(String telegramId, Long chatId, String text, String username) {
        Optional<User> userOptional = findUserByTelegramIdUseCase.execute(telegramId);
        String messageAnswer;

        if (userOptional.isEmpty()) {
            User newUser = User.builder()
                    .telegramId(telegramId)
                    .name(username).email(telegramId.repeat(2) + "@telegram.zaldo")
                    .password(UUID.randomUUID().toString())
                    .build();
            createUserUseCase.execute(newUser);
            messageAnswer = String.format(
                    "👋 Olá, <b>%s</b>! Seja bem-vindo ao <b>Zaldo</b>. Sua conta já está ativa! 🚀\n\n" +
                            "Quer levar sua gestão para o próximo nível? Configure um e-mail e senha para acessar nossa <b>versão Web</b>.\n\n" +
                            "Basta clicar aqui:",
                    username
            );

            InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                    .keyboardRow(new InlineKeyboardRow(
                                    InlineKeyboardButton.builder()
                                            .text("Configurar Acesso \uD83D\uDD11")
                                            .callbackData("BTN_LOGIN")
                                            .build()
                            )
                    ).build();

            return SendMessage.builder()
                    .text(messageAnswer)
                    .chatId(chatId)
                    .replyMarkup(markup)
                    .parseMode("HTML")
                    .build();
        } else {
            messageAnswer = "Olá novamente, <b>" + username + "</b>! Você já tem cadastro.";
        }

        return SendMessage.builder()
                .text(messageAnswer)
                .chatId(chatId)
                .parseMode("HTML")
                .build();
    }
}
