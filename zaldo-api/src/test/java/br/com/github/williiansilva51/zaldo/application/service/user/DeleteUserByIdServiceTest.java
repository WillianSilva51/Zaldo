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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserByIdServiceTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private DeleteUserByIdService deleteUserByIdService;

    private User createValidUser() {
        return User
                .builder()
                .id("userId123")
                .build();
    }

    @Test
    @DisplayName("Deve deletar o usuário quando existir o id")
    void shouldDeleteUserSuccessfully() {
        User user = createValidUser();

        String id = user.getId();

        when(userRepositoryPort.findById(id)).thenReturn(Optional.of(user));

        deleteUserByIdService.execute(id);

        verify(userRepositoryPort).findById(id);
        verify(userRepositoryPort).deleteById(id);

        verifyNoMoreInteractions(userRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'ResourceNotFoundException' se não existir o usuário para deletar")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        String id = "userId123";

        when(userRepositoryPort.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteUserByIdService.execute(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado com ID: " + id);

        verify(userRepositoryPort).findById(id);
        verify(userRepositoryPort, never()).deleteById(id);
        verifyNoMoreInteractions(userRepositoryPort);
    }

}
