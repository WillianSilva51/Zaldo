package br.com.github.williiansilva51.zaldo.core.ports.in.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;

import java.time.LocalDate;

public interface ListTransactionUseCase {
    Paginated<Transaction> execute(TransactionType type, LocalDate date, int page, int size, String sort, DirectionOrder direction);
}
