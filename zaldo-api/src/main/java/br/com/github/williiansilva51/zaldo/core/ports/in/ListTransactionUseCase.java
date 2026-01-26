package br.com.github.williiansilva51.zaldo.core.ports.in;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;

import java.time.LocalDate;
import java.util.List;

public interface ListTransactionUseCase {
    List<Transaction> execute(TransactionType type, LocalDate date);
}
