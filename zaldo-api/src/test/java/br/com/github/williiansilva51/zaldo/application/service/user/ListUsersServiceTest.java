package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUsersServiceTest {
    private final List<User> users = List.of(createValidUser("id1", "user1@gmail.com"),
            createValidUser("id2", "user2@gmail.com"),
            createValidUser("id3", "admin@gmail.com"));
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @InjectMocks
    private ListUsersService listUsersService;

    private User createValidUser(String id, String email) {
        return User
                .builder()
                .id(id)
                .email(email)
                .build();
    }

    @Test
    @DisplayName("Deve retornar a lista vazia quando não ter usuários")
    void shouldReturnEmptyListWhenThereAreNoUsers() {
        when(userRepositoryPort.findAll()).thenReturn(List.of());

        List<User> users = listUsersService.execute(null);

        assertThat(users).isEmpty();

        verify(userRepositoryPort).findAll();
        verifyNoMoreInteractions(userRepositoryPort);
    }

    @Test
    @DisplayName("Deve retornar todos os usuários quando não houver filtro")
    void shouldReturnAllUsersWhenNoFilterIsProvided() {
        when(userRepositoryPort.findAll())
                .thenReturn(users);

        List<User> usersResult = listUsersService.execute(null);

        assertThat(usersResult).hasSize(3).containsAll(users);
        verify(userRepositoryPort).findAll();
        verifyNoMoreInteractions(userRepositoryPort);
    }

    @Test
    @DisplayName("Deve buscar usuários por fragmento de e-mail quando o filtro for fornecido")
    void shouldReturnUsersWithEmailFragment() {
        String emailFragment = "admin";

        List<User> filteredList = List.of(users
                .stream()
                .filter(user -> user.getEmail().contains("admin"))
                .findFirst()
                .get());

        when(userRepositoryPort.findByEmailContaining(emailFragment))
                .thenReturn(filteredList);

        List<User> result = listUsersService.execute(emailFragment);

        assertThat(result).hasSize(1).containsAll(filteredList);
        verify(userRepositoryPort).findByEmailContaining(emailFragment);
        verify(userRepositoryPort, never()).findAll();
        verifyNoMoreInteractions(userRepositoryPort);
    }

}
