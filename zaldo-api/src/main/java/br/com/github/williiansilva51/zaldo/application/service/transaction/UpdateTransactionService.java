package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.UpdateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateTransactionService implements UpdateTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public Transaction execute(Long id, Transaction transaction) {
        Transaction existingTransaction = transactionRepositoryPort
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada para atualização: " + id));

        existingTransaction.update(transaction);

        if (!existingTransaction.isPositive()) {
            throw new IllegalArgumentException("A transação não pode ter valor negativo ou zero após atualização.");
        }

        return transactionRepositoryPort.save(existingTransaction);
    }
}
