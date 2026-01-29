package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletPersistenceMapper implements PersistenceMapper<WalletEntity, Wallet> {
    private final UserPersistenceMapper userMapper;

    @Override
    public WalletEntity toEntity(Wallet domain) {
        if (domain == null) {
            return null;
        }

        return WalletEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .user(userMapper.toEntity(domain.getUser()))
                .build();
    }

    @Override
    public Wallet toDomain(WalletEntity entity) {
        if (entity == null) {
            return null;
        }

        return Wallet.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .user(userMapper.toDomain(entity.getUser()))
                .build();
    }
}
