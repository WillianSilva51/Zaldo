package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.ListUsersUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersService implements ListUsersUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public List<User> execute(String email) {
        if (email != null) {
            return userRepositoryPort.findByEmailContaining(email);
        }

        return userRepositoryPort.findAll();
    }
}
