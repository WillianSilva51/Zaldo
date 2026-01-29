package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {
    private final SpringDataTransactionRepository springDataTransactionRepository;

    private <T> Paginated<T> toDomainPage(Page<T> springPage) {
        return new Paginated<>(
                springPage.getContent(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.getNumber());
    }

    private Pageable createPageRequest(int page, int size, String sort, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        return PageRequest.of(page, size, Sort.by(sortDirection, sort));
    }

    @Override
    public Transaction save(Transaction transaction) {
        return springDataTransactionRepository.save(transaction);
    }

    @Override
    public Paginated<Transaction> findAll(int page, int size, String sort, String direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findAll(pageable));
    }

    @Override
    public Paginated<Transaction> findByTransactionTypeAndDate(TransactionType type, LocalDate date, int page, int size, String sort, String direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findByTypeAndDate(type, date, pageable));
    }

    @Override
    public Paginated<Transaction> findByTransactionType(TransactionType type, int page, int size, String sort, String direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findByType(type, pageable));
    }

    @Override
    public Paginated<Transaction> findByDate(LocalDate date, int page, int size, String sort, String direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findByDate(date, pageable));
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
