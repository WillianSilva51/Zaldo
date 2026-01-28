package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository springDataUserRepository;

    @Override
    public User save(User user) {
        return springDataUserRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return springDataUserRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email);
    }

    @Override
    public List<User> findByEmailContaining(String emailFragment) {
        return springDataUserRepository.findByEmailContaining(emailFragment);
    }

    @Override
    public void deleteById(String id) {
        springDataUserRepository.deleteById(id);
    }
}
