package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.user;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface SpringDataUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
