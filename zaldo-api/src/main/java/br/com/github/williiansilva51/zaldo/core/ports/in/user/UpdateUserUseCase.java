package br.com.github.williiansilva51.zaldo.core.ports.in.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;

public interface UpdateUserUseCase {
    User execute(String id, User user);
}
