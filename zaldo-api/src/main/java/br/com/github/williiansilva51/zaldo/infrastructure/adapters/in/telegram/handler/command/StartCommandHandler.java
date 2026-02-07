package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.CreateUserUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.FindUserByTelegramIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
            messageAnswer = "\uD83D\uDC4B Olá, " + username + "! Sua conta foi criada com sucesso no Zaldo.";
        } else {
            messageAnswer = "Olá novamente, " + username + "! Você já tem cadastro.";
        }

        return SendMessage.builder()
                .text(messageAnswer)
                .chatId(chatId)
                .build();
    }
}
