package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SpringDataTransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByDate(LocalDate date);
}
