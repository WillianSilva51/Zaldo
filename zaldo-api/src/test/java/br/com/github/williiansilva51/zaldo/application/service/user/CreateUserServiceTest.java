package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserService service;

    private User createValidUser() {
        return User.builder()
                .name("user-123")
                .email("user@email.com")
                .password("user-password")
                .build();
    }

    @Test
    @DisplayName("Deve criar o usuário com senha criptografada quando o e-mail for único")
    void shouldCreateUserSuccessfully() {
        User user = createValidUser();

        when(userRepositoryPort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("user-password")).thenReturn("encoded-password");
        when(userRepositoryPort.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.execute(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepositoryPort).findByEmail(user.getEmail());
        verify(passwordEncoder).encode("user-password");
        verify(userRepositoryPort).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();

        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");

        verifyNoMoreInteractions(userRepositoryPort, passwordEncoder);
    }

    @Test
    @DisplayName("Deve lançar a exceção 'DomainValidationException' quando o o e-mail não for único")
    void shouldThrowDomainValidationExceptionWhenEmailIsNotUnique() {
        User user = createValidUser();

        when(userRepositoryPort.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(User.builder().build()));

        assertThatThrownBy(() -> service.execute(user))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Já existe um usuário com este e-mail.");

        verify(userRepositoryPort).findByEmail(user.getEmail());
        verify(userRepositoryPort, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepositoryPort, passwordEncoder);
    }
}
