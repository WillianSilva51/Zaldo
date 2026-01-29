package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.transaction;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Page<TransactionEntity> findByType(TransactionType type, Pageable pageable);

    Page<TransactionEntity> findByDate(LocalDate date, Pageable pageable);

    Page<TransactionEntity> findByTypeAndDate(TransactionType type, LocalDate date, Pageable pageable);
}
