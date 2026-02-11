package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.FindTransactionByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindTransactionByIdService implements FindTransactionByIdUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public Transaction execute(Long id) {
        return transactionRepositoryPort
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada com ID: " + id));
    }
}
