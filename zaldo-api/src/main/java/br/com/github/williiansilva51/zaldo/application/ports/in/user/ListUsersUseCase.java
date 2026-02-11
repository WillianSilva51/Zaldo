package br.com.github.williiansilva51.zaldo.application.ports.in.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;

import java.util.List;

public interface ListUsersUseCase {
    List<User> execute(String emailFragment);
}
