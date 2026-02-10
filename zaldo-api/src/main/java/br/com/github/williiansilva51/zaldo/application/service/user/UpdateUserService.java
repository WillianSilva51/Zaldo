package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.UpdateUserUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserService implements UpdateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public User execute(String id, User user) {
        User existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para atualização: " + id));

        String newEmail = user.getEmail();
        if (newEmail != null && !newEmail.equals(existingUser.getEmail())) {
            userRepositoryPort.findByEmail(newEmail)
                    .filter(foundUser -> !foundUser.getId().equals(id))
                    .ifPresent(foundUser -> {
                        throw new IllegalArgumentException("Email já está em uso: " + newEmail);
                    });
        }

        // TODO: Futuramente aqui entra o Hash da Senha (BCrypt)

        existingUser.update(user);

        return userRepositoryPort.save(existingUser);
    }
}
