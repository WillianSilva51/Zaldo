package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {
    private final SpringDataTransactionRepository springDataTransactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return springDataTransactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return springDataTransactionRepository.findAll();
    }

    @Override
    public List<Transaction> findByTransactionTypeAndDate(TransactionType type, LocalDate date) {
        return springDataTransactionRepository.findByTypeAndDate(type, date);
    }

    @Override
    public List<Transaction> findByTransactionType(TransactionType type) {
        return springDataTransactionRepository.findByType(type);
    }

    @Override
    public List<Transaction> findByDate(LocalDate date) {
        return springDataTransactionRepository.findByDate(date);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return springDataTransactionRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) throws ResourceNotFoundException {
        springDataTransactionRepository.deleteById(id);
    }
}
