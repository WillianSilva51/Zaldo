package br.com.github.williiansilva51.zaldo.core.ports.in.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.TransactionSortField;

public interface FindTransactionByWalletIdUseCase {
    Paginated<Transaction> execute(Long walletId, int page, int size, TransactionSortField sort, DirectionOrder direction);
}
