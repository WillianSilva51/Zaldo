package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.GetBalanceByWalletAndUserUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateTransactionService implements CreateTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final FindWalletByIdUseCase findWalletByIdUseCase;
    private final GetBalanceByWalletAndUserUseCase getBalanceByWalletAndUserUseCase;

    @Override
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

        if (!transaction.isIncome()) {
            String userId = wallet.getUser().getId();
            BigDecimal value = getBalanceByWalletAndUserUseCase.execute(walletId, userId);

            BigDecimal result = value.subtract(transaction.getAmount());

            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessRuleException("O valor da Transação irá negativar o saldo da carteira. Tente novamente com um valor menor.");
            }
        }

        return transactionRepositoryPort.save(transaction);
    }
}
