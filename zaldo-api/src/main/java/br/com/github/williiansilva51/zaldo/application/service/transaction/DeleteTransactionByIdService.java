package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.DeleteTransactionByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteTransactionByIdService implements DeleteTransactionByIdUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public void execute(Long id) {
        if (transactionRepositoryPort.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Transação não encontrada com ID: " + id);
        }
        transactionRepositoryPort.deleteById(id);
    }
}
