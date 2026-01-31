package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.WalletEntity;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper.WalletPersistenceMapper;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements WalletRepositoryPort {
    private final SpringDataWalletRepository walletRepository;
    private final WalletPersistenceMapper walletMapper;
    private final PageUtils pageUtils;

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = walletMapper.toEntity(wallet);
        WalletEntity savedEntity = walletRepository.save(entity);
        return walletMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id)
                .map(walletMapper::toDomain);
    }

    @Override
    public Paginated<Wallet> findByUserId(String userId, int page, int size, WalletSortField sort, DirectionOrder direction) {
        Pageable pageable = pageUtils.createPageRequest(page, size, sort, direction);

        return pageUtils.toDomainPage(walletRepository.findByUserId(userId, pageable)
                .map(walletMapper::toDomain));
    }

    @Override
    public void deleteById(Long id) {

    }
}
