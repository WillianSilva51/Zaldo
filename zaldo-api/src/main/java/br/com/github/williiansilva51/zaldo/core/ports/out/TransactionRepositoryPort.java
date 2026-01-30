package br.com.github.williiansilva51.zaldo.core.ports.out;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;

import java.time.LocalDate;
import java.util.Optional;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);

    Paginated<Transaction> findAll(int page, int size, String sort, DirectionOrder direction);

    Paginated<Transaction> findByTransactionTypeAndDate(TransactionType type, LocalDate date, int page, int size, String sort, DirectionOrder direction);

    Paginated<Transaction> findByTransactionType(TransactionType type, int page, int size, String sort, DirectionOrder direction);

    Paginated<Transaction> findByDate(LocalDate date, int page, int size, String sort, DirectionOrder direction);

    Optional<Transaction> findById(Long id);

    void deleteById(Long id);
}
