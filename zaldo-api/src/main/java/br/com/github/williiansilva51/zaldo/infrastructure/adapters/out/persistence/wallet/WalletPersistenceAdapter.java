package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.WalletEntity;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper.WalletPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements WalletRepositoryPort {
    private final SpringDataWalletRepository walletRepository;
    private final WalletPersistenceMapper walletMapper;

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = walletMapper.toEntity(wallet);
        WalletEntity savedEntity = walletRepository.save(entity);
        return walletMapper.toDomain(savedEntity);
    }
}
