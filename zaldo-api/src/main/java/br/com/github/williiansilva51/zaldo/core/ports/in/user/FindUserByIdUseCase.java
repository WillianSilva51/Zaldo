package br.com.github.williiansilva51.zaldo.core.ports.in.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;

public interface FindUserByIdUseCase {
    User execute(String id);
}
