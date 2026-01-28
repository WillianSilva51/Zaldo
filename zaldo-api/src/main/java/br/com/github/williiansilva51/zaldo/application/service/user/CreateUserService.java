package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.CreateUserUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User execute(User user) {
        if (userRepositoryPort.findByEmail(user.getEmail()).isPresent()) {
            throw new DomainValidationException("Já existe um usuário com este e-mail.");
        }

        // TODO: Futuramente aqui entra o Hash da Senha (BCrypt)

        return userRepositoryPort.save(user);
    }
}
