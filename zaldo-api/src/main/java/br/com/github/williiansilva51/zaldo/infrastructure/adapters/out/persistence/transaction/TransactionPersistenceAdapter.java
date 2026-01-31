package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.transaction;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.enums.sort.TransactionSortField;
import br.com.github.williiansilva51.zaldo.core.ports.out.TransactionRepositoryPort;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.TransactionEntity;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper.TransactionPersistenceMapper;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {
    private final SpringDataTransactionRepository springDataTransactionRepository;
    private final TransactionPersistenceMapper mapper;
    private final PageUtils pageUtils;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);

        TransactionEntity savedEntity = springDataTransactionRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Paginated<Transaction> findAll(int page, int size, TransactionSortField sort, DirectionOrder direction) {
        Pageable pageable = pageUtils.createPageRequest(page, size, sort, direction);
        return pageUtils.toDomainPage(springDataTransactionRepository.findAll(pageable).map(mapper::toDomain));
    }

    @Override
    public Paginated<Transaction> findByTransactionTypeAndDate(TransactionType type, LocalDate date, int page, int size, TransactionSortField sort, DirectionOrder direction) {
        Pageable pageable = pageUtils.createPageRequest(page, size, sort, direction);
        return pageUtils.toDomainPage(springDataTransactionRepository.findByTypeAndDate(type, date, pageable).map(mapper::toDomain));
    }

    @Override
    public Paginated<Transaction> findByTransactionType(TransactionType type, int page, int size, TransactionSortField sort, DirectionOrder direction) {
        Pageable pageable = pageUtils.createPageRequest(page, size, sort, direction);
        return pageUtils.toDomainPage(springDataTransactionRepository.findByType(type, pageable).map(mapper::toDomain));
    }

    @Override
    public Paginated<Transaction> findByDate(LocalDate date, int page, int size, TransactionSortField sort, DirectionOrder direction) {
        Pageable pageable = pageUtils.createPageRequest(page, size, sort, direction);
        return pageUtils.toDomainPage(springDataTransactionRepository.findByDate(date, pageable).map(mapper::toDomain));
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
