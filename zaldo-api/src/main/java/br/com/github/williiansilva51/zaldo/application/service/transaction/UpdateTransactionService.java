package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.UpdateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.GetBalanceByWalletAndUserUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.exceptions.BusinessRuleException;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateTransactionService implements UpdateTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final GetBalanceByWalletAndUserUseCase getBalanceByWalletAndUserUseCase;

    @Override
    public Transaction execute(Long id, Transaction transaction) {
        Transaction existingTransaction = transactionRepositoryPort
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada para atualização: " + id));

        BigDecimal oldAmount = existingTransaction.getAmount();
        boolean oldIsIncome = existingTransaction.isIncome();

        existingTransaction.update(transaction);

        if (!existingTransaction.isPositive()) {
            throw new IllegalArgumentException("A transação não pode ter valor negativo ou zero após atualização.");
        }

        Long walletId = existingTransaction.getWallet().getId();
        String userId = existingTransaction.getWallet().getUser().getId();
        BigDecimal currentBalance = getBalanceByWalletAndUserUseCase.execute(walletId, userId);

        BigDecimal balanceWithoutOld = oldIsIncome ?
                currentBalance.subtract(oldAmount) :
                currentBalance.add(oldAmount);

        BigDecimal finalBalance = existingTransaction.isIncome()
                ? balanceWithoutOld.add(existingTransaction.getAmount())
                : balanceWithoutOld.subtract(existingTransaction.getAmount());

        if (finalBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Operação cancelada: o saldo final seria negativo (" + finalBalance + ")");
        }

        return transactionRepositoryPort.save(existingTransaction);
    }
}
