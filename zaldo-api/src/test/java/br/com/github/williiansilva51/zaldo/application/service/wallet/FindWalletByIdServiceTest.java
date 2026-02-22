package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindWalletByIdServiceTest {
    @Mock
    private WalletRepositoryPort walletRepositoryPort;

    @InjectMocks
    private FindWalletByIdService findWalletByIdService;

    private Wallet createValidWallet() {
        return Wallet.builder()
                .id(1L)
                .name("nome")
                .description("descrição")
                .build();
    }

    @Test
    @DisplayName("Deve retornar a carteira se o id existir")
    void shouldReturnWalletSuccessfully() {
        Wallet wallet = createValidWallet();

        when(walletRepositoryPort.findById(wallet.getId())).thenReturn(Optional.of(wallet));

        assertThat(findWalletByIdService.execute(wallet.getId())).isEqualTo(wallet);

        verify(walletRepositoryPort).findById(wallet.getId());
        verifyNoMoreInteractions(walletRepositoryPort);

    }

    @Test
    @DisplayName("Deve retornar 'ResourceNotFoundException', caso não encontre a carteira")
    void shouldThrowResourceNotFoundExceptionWhenWalletDoesNotExist() {
        Long walletId = 1L;

        when(walletRepositoryPort.findById(walletId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findWalletByIdService.execute(walletId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Carteira não encontrada com ID: " + walletId);

        verify(walletRepositoryPort).findById(walletId);
        verifyNoMoreInteractions(walletRepositoryPort);
    }
}
