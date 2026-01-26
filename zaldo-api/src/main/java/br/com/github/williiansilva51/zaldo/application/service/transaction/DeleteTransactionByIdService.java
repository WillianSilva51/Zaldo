package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.DeleteTransactionByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTransactionByIdService implements DeleteTransactionByIdUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    @Transactional
    public void execute(Long id) {
        if (transactionRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Transação não encontrada com ID: " + id);
        }
        transactionRepositoryPort.deleteById(id);
    }
}
