package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.enums.sort.TransactionSortField;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.ListTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListTransactionService implements ListTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public Paginated<Transaction> execute(TransactionType type, LocalDate date, int page, int size, TransactionSortField sort, DirectionOrder direction) {
        if (type != null && date != null) {
            return transactionRepositoryPort.findByTransactionTypeAndDate(type, date, page, size, sort, direction);
        }

        if (type != null) {
            return transactionRepositoryPort.findByTransactionType(type, page, size, sort, direction);
        }

        if (date != null) {
            return transactionRepositoryPort.findByDate(date, page, size, sort, direction);
        }

        return transactionRepositoryPort.findAll(page, size, sort, direction);
    }
}
