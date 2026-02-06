package br.com.github.williiansilva51.zaldo.application.service.user;

import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.in.user.DeleteUserByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteUserByIdService implements DeleteUserByIdUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void execute(String id) {
        if (userRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
        }

        userRepositoryPort.deleteById(id);
    }
}
