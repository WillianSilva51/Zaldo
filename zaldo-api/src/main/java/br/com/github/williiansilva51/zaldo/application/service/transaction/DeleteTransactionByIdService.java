package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.DeleteTransactionByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteTransactionByIdService implements DeleteTransactionByIdUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final FindWalletByIdUseCase findWalletByIdUseCase;

    @Override
    public void execute(Long id, String authenticatedUserId) {
        Transaction transaction = transactionRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada com ID: " + id));

        Wallet wallet = findWalletByIdUseCase.execute(transaction.getWallet().getId());

        if (!wallet.getUser().getId().equals(authenticatedUserId)) {
            throw new DomainValidationException("Você não tem permissão para apagar esta Transação.");
        }

        transactionRepositoryPort.deleteById(id);
    }
}
