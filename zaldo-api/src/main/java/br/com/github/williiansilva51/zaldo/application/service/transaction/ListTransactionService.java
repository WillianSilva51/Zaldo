package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.ListTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTransactionService implements ListTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public List<Transaction> execute(TransactionType type, LocalDate date) {
        if (type != null && date != null) {
            return transactionRepositoryPort.findByTransactionTypeAndDate(type, date);
        }

        if (type != null) {
            return transactionRepositoryPort.findByTransactionType(type);
        }

        if (date != null) {
            return transactionRepositoryPort.findByDate(date);
        }

        return transactionRepositoryPort.findAll();
    }
}
