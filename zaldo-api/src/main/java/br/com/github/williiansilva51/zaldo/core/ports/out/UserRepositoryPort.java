package br.com.github.williiansilva51.zaldo.core.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    List<User> findAll();

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    void deleteById(String id);
}
