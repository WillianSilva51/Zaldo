package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.TransactionEntity;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper.TransactionPersistenceMapper;
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
    private final TransactionPersistenceMapper mapper;

    private <T> Paginated<T> toDomainPage(Page<T> springPage) {
        return new Paginated<>(
                springPage.getContent(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.getNumber());
    }

    private Pageable createPageRequest(int page, int size, String sort, DirectionOrder direction) {
        return PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.valueOf(direction.name()), sort));
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);

        TransactionEntity savedEntity = springDataTransactionRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Paginated<Transaction> findAll(int page, int size, String sort, DirectionOrder direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findAll(pageable).map(mapper::toDomain));
    }

    @Override
    public Paginated<Transaction> findByTransactionTypeAndDate(TransactionType type, LocalDate date, int page, int size, String sort, DirectionOrder direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findByTypeAndDate(type, date, pageable).map(mapper::toDomain));
    }

    @Override
    public Paginated<Transaction> findByTransactionType(TransactionType type, int page, int size, String sort, DirectionOrder direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findByType(type, pageable).map(mapper::toDomain));
    }

    @Override
    public Paginated<Transaction> findByDate(LocalDate date, int page, int size, String sort, DirectionOrder direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);
        return toDomainPage(springDataTransactionRepository.findByDate(date, pageable).map(mapper::toDomain));
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return springDataTransactionRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        springDataTransactionRepository.deleteById(id);
    }
}
