package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByIdServiceTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private FindUserByIdService findUserByIdService;

    private User createValidUser() {
        return User
                .builder()
                .id("userId123")
                .build();
    }

    @Test
    @DisplayName("Deve retornar o usuário caso ele exista com o id")
    void shouldReturnUserSuccessfully() {
        User user = createValidUser();
        String userId = user.getId();

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));
        User foundUser = findUserByIdService.execute(userId);

        verify(userRepositoryPort).findById(userId);
        verifyNoMoreInteractions(userRepositoryPort);

        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Deve lançar 'ResourceNotFoundException' se não existir o usuário")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        String userId = "userId123";

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findUserByIdService.execute(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado: " + userId);

        verify(userRepositoryPort).findById(userId);
        verifyNoMoreInteractions(userRepositoryPort);
    }
}
