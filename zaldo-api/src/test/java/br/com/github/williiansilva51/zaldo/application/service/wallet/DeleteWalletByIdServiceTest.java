package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteWalletByIdServiceTest {
    private final String userId = "userId1";
    @Mock
    private WalletRepositoryPort walletRepositoryPort;
    @Mock
    private FindWalletByIdUseCase findWalletByIdUseCase;
    @InjectMocks
    private DeleteWalletByIdService deleteWalletByIdService;

    private Wallet createValidWallet() {
        return Wallet.builder()
                .id(1L)
                .name("wallet1")
                .description("money")
                .user(User.builder().id(userId).build())
                .build();
    }

    @Test
    @DisplayName("Deve apagar a carteira se ela pertencer ao usuário")
    void shouldDeleteWalletSuccessfully() {
        Wallet wallet = createValidWallet();

        when(findWalletByIdUseCase.execute(wallet.getId())).thenReturn(wallet);

        deleteWalletByIdService.execute(wallet.getId(), userId);

        verify(findWalletByIdUseCase).execute(wallet.getId());
        verify(walletRepositoryPort).deleteById(wallet.getId());

        verifyNoMoreInteractions(walletRepositoryPort, findWalletByIdUseCase);
    }

    @Test
    @DisplayName("Deve lançar 'DomainValidationException' caso a carteira não pertença ao usuário solicitado")
    void shouldThrowDomainValidationExceptionWhenWalletDoesNotBelongToUser() {
        Wallet wallet = createValidWallet();
        String userIdFalse = "userIdFalse";

        when(findWalletByIdUseCase.execute(wallet.getId())).thenReturn(wallet);


        assertThatThrownBy(() -> deleteWalletByIdService.execute(wallet.getId(), userIdFalse))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Você não tem permissão para deletar esta carteira.");

        verify(findWalletByIdUseCase).execute(wallet.getId());
        verify(walletRepositoryPort, never()).deleteById(wallet.getId());
        verifyNoMoreInteractions(walletRepositoryPort, findWalletByIdUseCase);
    }
}
