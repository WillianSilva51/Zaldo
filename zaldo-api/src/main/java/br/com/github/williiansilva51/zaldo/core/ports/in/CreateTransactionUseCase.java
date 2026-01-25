package br.com.github.williiansilva51.zaldo.core.ports.in;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;

public interface CreateTransactionUseCase {
    Transaction execute(Transaction transaction);
}
