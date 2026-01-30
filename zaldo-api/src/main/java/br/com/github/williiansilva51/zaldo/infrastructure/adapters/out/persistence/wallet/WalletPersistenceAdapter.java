package br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.wallet;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import br.com.github.williiansilva51.zaldo.core.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.entity.WalletEntity;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.out.persistence.mapper.WalletPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements WalletRepositoryPort {
    private final SpringDataWalletRepository walletRepository;
    private final WalletPersistenceMapper walletMapper;

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
    public Wallet save(Wallet wallet) {
        WalletEntity entity = walletMapper.toEntity(wallet);
        WalletEntity savedEntity = walletRepository.save(entity);
        return walletMapper.toDomain(savedEntity);
    }

    @Override
    public Wallet findById(Long id) {
        return walletRepository.findById(id)
                .map(walletMapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira não encontrada com ID: " + id));
    }

    @Override
    public Paginated<Wallet> findByUserId(String userId, int page, int size, String sort, DirectionOrder direction) {
        Pageable pageable = createPageRequest(page, size, sort, direction);

        return toDomainPage(walletRepository.findByUserId(userId, pageable)
                .map(walletMapper::toDomain));
    }
}
