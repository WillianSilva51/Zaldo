package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceMapper implements PersistenceMapper<TransactionEntity, Transaction> {
    private final WalletPersistenceMapper walletMapper;

    @Override
    public TransactionEntity toEntity(Transaction domain) {
        if (domain == null) return null;

        return TransactionEntity.builder()
                .id(domain.getId())
                .amount(domain.getAmount())
                .description(domain.getDescription())
                .type(domain.getType())
                .date(domain.getDate())
                .wallet(walletMapper.toEntity(domain.getWallet()))
                .build();
    }

    @Override
    public Transaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;

        return Transaction.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .type(entity.getType())
                .date(entity.getDate())
                .wallet(walletMapper.toDomain(entity.getWallet()))
                .build();
    }
}
