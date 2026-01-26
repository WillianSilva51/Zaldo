package br.com.github.williiansilva51.zaldo.core.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);

    List<Transaction> findAll();

    List<Transaction> findByTransactionType(TransactionType type);

    List<Transaction> findByDate(LocalDate date);

    Optional<Transaction> findById(Long id);

    void deleteById(Long id);
}
