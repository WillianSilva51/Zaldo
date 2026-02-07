package br.com.github.williiansilva51.zaldo.core.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);

    List<User> findAll();

    Optional<User> findById(String id);

    Optional<User> findByTelegramId(String telegramId);

    Optional<User> findByEmail(String email);

    List<User> findByEmailContaining(String emailFragment);

    void deleteById(String id);
}
