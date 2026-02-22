package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBalanceByWalletAndUserServiceTest {
    @Mock
    private WalletRepositoryPort walletRepositoryPort;

    @InjectMocks
    private GetBalanceByWalletAndUserService getBalanceByWalletAndUserService;

    @Test
    @DisplayName("Deve mostrar o saldo da carteira do usuário do forma correta")
    void shouldShowWalletBalanceCorrectly() {
        Long walletId = 1L;
        String userId = "userId";

        when(walletRepositoryPort.getTotalBalanceByWalletAndUser(walletId, userId)).thenReturn(BigDecimal.valueOf(1000));

        BigDecimal balance = getBalanceByWalletAndUserService.execute(walletId, userId);

        assertThat(balance).isEqualTo(BigDecimal.valueOf(1000));

        verify(walletRepositoryPort).getTotalBalanceByWalletAndUser(walletId, userId);
        verifyNoMoreInteractions(walletRepositoryPort);
    }
}
