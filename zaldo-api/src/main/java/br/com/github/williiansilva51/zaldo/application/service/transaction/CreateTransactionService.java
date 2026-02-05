package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final FindWalletByIdUseCase findWalletByIdUseCase;

    @Override
    @Transactional
    public Transaction execute(Transaction transaction) {
        Long walletId = transaction.getWallet().getId();
        Wallet wallet = findWalletByIdUseCase.execute(walletId);

        transaction.setWallet(wallet);

        transaction.validateState();

        return transactionRepositoryPort.save(transaction);
    }
}
