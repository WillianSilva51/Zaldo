package br.com.github.williiansilva51.zaldo.core.ports.in;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;

import java.util.List;

public interface ListTransactionUseCase {
    List<Transaction> execute();
}
