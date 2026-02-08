package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTransactionService implements CreateTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final FindWalletByIdUseCase findWalletByIdUseCase;

    @Override
    @Transactional
    public Transaction execute(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("A transação não deve ser nula");
        }

        Wallet transactionWallet = transaction.getWallet();

        if (transactionWallet == null || transactionWallet.getId() == null) {
            throw new IllegalArgumentException("A carteira de transações e o ID da carteira não devem ser nulos");
        }

        Long walletId = transactionWallet.getId();
        Wallet wallet = findWalletByIdUseCase.execute(walletId);

        transaction.setWallet(wallet);

        transaction.validateState();

        return transactionRepositoryPort.save(transaction);
    }
}
