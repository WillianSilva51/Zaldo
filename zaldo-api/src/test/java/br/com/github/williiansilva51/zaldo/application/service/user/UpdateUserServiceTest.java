package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserService updateUserService;

    private User existingUser;
    private User updateRequest;
    private String userId;

    @BeforeEach
    void setUp() {
        existingUser = createValidUser("user-name", "user@email.com", "user-password");
        updateRequest = createValidUser("newUser", "newUser@email.com", "newUser-password");
        userId = existingUser.getId();
    }

    private User createValidUser(String name, String email, String password) {
        return User
                .builder()
                .id("id1")
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    @DisplayName("Deve atualizar o usuário quando tudo estiver correto")
    void shouldUpdateUserSuccessfully() {
        String rawPassword = updateRequest.getPassword();
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.findByEmail(updateRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded-password");
        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        updateUserService.execute(userId, updateRequest);

        verify(userRepositoryPort).findById(userId);
        verify(userRepositoryPort).findByEmail(updateRequest.getEmail());

        verify(passwordEncoder).encode(rawPassword);

        verify(userRepositoryPort).save(argThat(savedUser -> {
            assertThat(savedUser.getId()).isEqualTo(updateRequest.getId());
            assertThat(savedUser.getName()).isEqualTo(updateRequest.getName());
            assertThat(savedUser.getEmail()).isEqualTo(updateRequest.getEmail().toLowerCase());
            assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
            return true;
        }));

        verifyNoMoreInteractions(userRepositoryPort, passwordEncoder);
    }

    @Test
    @DisplayName("Deve lançar 'ResourceNotFoundException' se não existir o usuário")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateUserService.execute(userId, existingUser))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado para atualização: " + userId);

        verify(userRepositoryPort).findById(userId);
        verifyNoMoreInteractions(userRepositoryPort, passwordEncoder);
    }

    @Test
    @DisplayName("Deve lançar 'IllegalArgumentException' se já existir um outro usuário com o novo email")
    void shouldThrowIllegalArgumentExceptionWhenUserEmailDoesExists() {
        String updateEmail = updateRequest.getEmail();

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.findByEmail(updateEmail))
                .thenReturn(Optional.of(User.builder().id("id2").email(updateEmail).build()));

        assertThatThrownBy(() -> updateUserService.execute(userId, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email já está em uso: " + updateEmail);


        verify(userRepositoryPort).findById(userId);
        verify(userRepositoryPort).findByEmail(updateEmail);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepositoryPort, never()).save(any());

        verifyNoMoreInteractions(userRepositoryPort, passwordEncoder);
    }

    @Test
    @DisplayName("Não deve validar e-mail se o e-mail for o mesmo do usuário atual")
    void shouldNotValidateEmailWhenEmailIsTheSame() {
        updateRequest.update(User.builder().email(existingUser.getEmail()).build());

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn("encoded-password");

        updateUserService.execute(userId, updateRequest);

        verify(userRepositoryPort, never()).findByEmail(anyString());

        verify(userRepositoryPort).save(existingUser);
    }

    @Test
    @DisplayName("Não deve chamar o passwordEncoder quando a nova senha for nula ou vazia")
    void shouldNotEncodePasswordWhenItIsBlank() {
        updateRequest = createValidUser("newUser", "newUser@email.com", null);

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepositoryPort.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepositoryPort.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        updateUserService.execute(userId, updateRequest);

        verify(passwordEncoder, never()).encode(anyString());
    }
}