package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.application.ports.in.user.CreateUserUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateUserService implements CreateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User execute(User user) {
        if (userRepositoryPort.findByEmail(user.getEmail()).isPresent()) {
            throw new DomainValidationException("Já existe um usuário com este e-mail.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.update(User.builder()
                .password(encodedPassword)
                .build());

        return userRepositoryPort.save(user);
    }
}
