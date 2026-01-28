package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public Transaction execute(Transaction transaction) {
        transaction.validateState();

        return transactionRepositoryPort.save(transaction);
    }
}
