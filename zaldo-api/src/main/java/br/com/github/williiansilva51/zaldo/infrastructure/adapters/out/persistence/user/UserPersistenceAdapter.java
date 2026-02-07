package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.UserEntity;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository springDataUserRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);

        UserEntity savedEntity = springDataUserRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return springDataUserRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByTelegramId(String telegramId) {
        return springDataUserRepository.findByTelegramId(telegramId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public List<User> findByEmailContaining(String emailFragment) {
        return springDataUserRepository.findByEmailContaining(emailFragment)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        springDataUserRepository.deleteById(id);
    }
}
