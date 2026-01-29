package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.user;

import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface SpringDataUserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByEmailContaining(String emailFragment);
}
