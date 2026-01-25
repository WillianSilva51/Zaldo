package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTransactionRepository extends JpaRepository<Transaction, Long> {
}
