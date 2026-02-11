package br.com.github.williiansilva51.zaldo.application.ports.in.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;

public interface UpdateTransactionUseCase {
    Transaction execute(Long id, Transaction transaction);
}
