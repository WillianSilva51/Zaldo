package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionPersistenceMapper implements PersistenceMapper<TransactionEntity, Transaction> {

    @Override
    public TransactionEntity toEntity(Transaction domain) {
        return TransactionEntity.builder()
                .id(domain.getId())
                .amount(domain.getAmount())
                .description(domain.getDescription())
                .type(domain.getType())
                .date(domain.getDate())
                .build();
    }

    @Override
    public Transaction toDomain(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .type(entity.getType())
                .date(entity.getDate())
                .build();
    }
}
