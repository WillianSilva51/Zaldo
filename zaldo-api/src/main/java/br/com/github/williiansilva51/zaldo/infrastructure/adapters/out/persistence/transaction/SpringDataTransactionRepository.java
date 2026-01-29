package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface SpringDataTransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByType(TransactionType type, Pageable pageable);

    Page<Transaction> findByDate(LocalDate date, Pageable pageable);

    Page<Transaction> findByTypeAndDate(TransactionType type, LocalDate date, Pageable pageable);
}
