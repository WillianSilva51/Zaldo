package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByTelegramIdServiceTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private FindUserByTelegramIdService findUserByTelegramIdService;

    private User createValidUser() {
        return User
                .builder()
                .telegramId("telegramId123")
                .build();
    }

    @Test
    @DisplayName("Deve retornar o usuário caso ele exista com o Telegram Id")
    void shouldReturnUserSuccessfully() {
        User user = createValidUser();
        String telegramId = user.getTelegramId();

        when(userRepositoryPort.findByTelegramId(telegramId)).thenReturn(Optional.of(user));

        User foundUser = findUserByTelegramIdService.execute(telegramId).get();

        verify(userRepositoryPort).findByTelegramId(telegramId);
        verifyNoMoreInteractions(userRepositoryPort);

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Deve retornar um Optional vazio caso o usuário não exista com o Telegram Id")
    void shouldReturnEmptyOptionalWhenUserDoesNotExist() {
        String telegramId = "id-inexistente";

        when(userRepositoryPort.findByTelegramId(telegramId)).thenReturn(Optional.empty());

        Optional<User> result = findUserByTelegramIdService.execute(telegramId);

        assertThat(result).isEmpty();

        verify(userRepositoryPort).findByTelegramId(telegramId);
        verifyNoMoreInteractions(userRepositoryPort);
    }
}
