package br.com.github.williiansilva51.zaldo.application.service.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.TransactionSortField;
import br.com.github.williiansilva51.zaldo.core.ports.in.transaction.FindTransactionByWalletIdUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindTransactionByWalletIdService implements FindTransactionByWalletIdUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public Paginated<Transaction> execute(Long walletId, int page, int size, TransactionSortField sort, DirectionOrder direction) {
        return transactionRepositoryPort.findByWalletId(walletId, page, size, sort, direction);
    }
}
