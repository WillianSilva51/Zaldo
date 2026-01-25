package br.com.github.williiansilva51.zaldo.application.service;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.ports.in.ListTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTrasactionService implements ListTransactionUseCase {
    private final TransactionRepositoryPort transactionRepositoryPort;

    @Override
    public List<Transaction> execute() {
        return transactionRepositoryPort.findAll();
    }
}
