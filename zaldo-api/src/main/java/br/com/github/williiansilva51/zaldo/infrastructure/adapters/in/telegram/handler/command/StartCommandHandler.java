package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.command;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.CreateUserUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.FindUserByTelegramIdUseCase;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

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
    public SendMessage execute(Message message, String username) {
        String telegramId = message.getFrom().getId().toString();
        Long chatId = message.getChatId();

        Optional<User> userOptional = findUserByTelegramIdUseCase.execute(telegramId);
        String messageAnswer;

        if (userOptional.isEmpty()) {
            User newUser = User.builder()
                    .telegramId(telegramId)
                    .name(username).email(telegramId.repeat(2) + "@telegram.zaldo")
                    .password(UUID.randomUUID().toString())
                    .build();
            createUserUseCase.execute(newUser);

            messageAnswer = "👋 Olá, <b>" + username + "</b>! Bem-vindo ao Zaldo. \n\nSua conta foi criada! Comece criando sua primeira carteira.";
        } else {
            messageAnswer = "👋 Olá de volta, <b>" + username + "</b>! \n\nComo posso ajudar suas finanças hoje?";
        }

        return SendMessage.builder()
                .text(messageAnswer)
                .chatId(chatId)
                .replyMarkup(MenuUtils.createMainKeyboard())
                .parseMode("HTML")
                .build();
    }
}
