package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper;

import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.CreateWalletRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet.UpdateWalletRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.WalletResponse;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper implements Mapper<Wallet, CreateWalletRequest, WalletResponse, UpdateWalletRequest> {

    @Override
    public Wallet toDomain(CreateWalletRequest request) {
        return Wallet.builder()
                .name(request.name())
                .description(request.description())
                .user(User.builder().id(request.userId()).build())
                .build();
    }

    @Override
    public Wallet toDomainByUpdate(UpdateWalletRequest request) {
        return Wallet.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    @Override
    public WalletResponse toResponse(Wallet domain) {
        return new WalletResponse(
                domain.getId(),
                domain.getName(),
                domain.getDescription(),
                domain.getUser().getId(),
                domain.getUser().getName()
        );
    }
}
