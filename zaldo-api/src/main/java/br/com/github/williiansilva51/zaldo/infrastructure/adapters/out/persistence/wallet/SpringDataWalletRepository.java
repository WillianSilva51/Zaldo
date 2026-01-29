package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.wallet;

import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataWalletRepository extends JpaRepository<WalletEntity, Long> {
}
