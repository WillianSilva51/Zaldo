package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.BusinessRuleException;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateWalletServiceTest {
    private final int maximumNumberOfWallets = 2;
    @Mock
    private WalletRepositoryPort walletRepositoryPort;
    @Mock
    private UserRepositoryPort userRepositoryPort;
    private CreateWalletService createWalletService;

    private User user;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        user = createValidUser();
        wallet = createValidWallet();
        createWalletService = new CreateWalletService(walletRepositoryPort, userRepositoryPort, maximumNumberOfWallets);
    }

    private User createValidUser() {
        return User.builder()
                .id("id1")
                .build();
    }

    private Wallet createValidWallet() {
        return Wallet.builder()
                .name("nome")
                .description("descrição")
                .user(user)
                .build();
    }

    @Test
    @DisplayName("Deve criar a carteira com nome único por usuário")
    void shouldCreateWalletSuccessfully() {
        when(userRepositoryPort.findById(user.getId())).thenReturn(Optional.of(user));
        when(walletRepositoryPort.countByUserId(user.getId())).thenReturn(0L);
        when(walletRepositoryPort.existsByUserIdAndName(user.getId(), wallet.getName()))
                .thenReturn(false);
        when(walletRepositoryPort.save(wallet)).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        createWalletService.execute(wallet);

        verify(userRepositoryPort).findById(user.getId());
        verify(walletRepositoryPort).countByUserId(user.getId());
        verify(walletRepositoryPort).existsByUserIdAndName(user.getId(), wallet.getName());

        verify(walletRepositoryPort).save(argThat(savedWallet -> {
            assertThat(savedWallet.getName()).isEqualTo(wallet.getName());
            assertThat(savedWallet.getDescription()).isEqualTo(wallet.getDescription());
            assertThat(savedWallet.getUser()).isEqualTo(user);
            assertThat(savedWallet.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
            return true;
        }));

        verifyNoMoreInteractions(userRepositoryPort, walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'ResourceNotFoundException' caso o usuário não exista")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        String userId = wallet.getUser().getId();

        when(userRepositoryPort.findById(user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createWalletService.execute(wallet))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado com ID: " + userId);

        verify(userRepositoryPort).findById(userId);
        verify(walletRepositoryPort, never()).save(any(Wallet.class));
        verifyNoMoreInteractions(userRepositoryPort, walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'BusinessRuleException' quando o limite de carteiras for atingido")
    void shouldThrowBusinessRuleExceptionWhenMaximumNumberOfWalletsIsReached() {
        Long currentWalletCount = 2L;

        when(userRepositoryPort.findById(user.getId())).thenReturn(Optional.of(user));
        when(walletRepositoryPort.countByUserId(user.getId())).thenReturn(currentWalletCount);

        assertThatThrownBy(() -> createWalletService.execute(wallet))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage(
                        String.format("Limite atingido. Você já possui %d/%d carteiras.",
                                currentWalletCount, maximumNumberOfWallets
                        ));

        verify(userRepositoryPort).findById(user.getId());
        verify(walletRepositoryPort).countByUserId(user.getId());
        verify(walletRepositoryPort, never()).save(any(Wallet.class));
        verifyNoMoreInteractions(userRepositoryPort, walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'BusinessRuleException' quando já existir carteira com o mesmo nome")
    void shouldThrowBusinessRuleExceptionWhenWalletNameAlreadyExists() {
        when(userRepositoryPort.findById(user.getId())).thenReturn(Optional.of(user));
        when(walletRepositoryPort.countByUserId(user.getId())).thenReturn(0L);
        when(walletRepositoryPort.existsByUserIdAndName(user.getId(), wallet.getName())).thenReturn(true);

        assertThatThrownBy(() -> createWalletService.execute(wallet))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage(String.format(
                        "Já existe uma carteira com o nome '%s' para esse usuário.",
                        wallet.getName()
                ));

        verify(userRepositoryPort).findById(user.getId());
        verify(walletRepositoryPort).countByUserId(user.getId());
        verify(walletRepositoryPort).existsByUserIdAndName(user.getId(), wallet.getName());
        verify(walletRepositoryPort, never()).save(any(Wallet.class));
        verifyNoMoreInteractions(userRepositoryPort, walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'BusinessRuleException' quando ocorrer erro de integridade no banco")
    void shouldThrowBusinessRuleExceptionWhenDataIntegrityViolationOccurs() {
        when(userRepositoryPort.findById(user.getId())).thenReturn(Optional.of(user));
        when(walletRepositoryPort.countByUserId(user.getId())).thenReturn(0L);
        when(walletRepositoryPort.existsByUserIdAndName(user.getId(), wallet.getName())).thenReturn(false);

        when(walletRepositoryPort.save(any(Wallet.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Unique constraint violation"));

        assertThatThrownBy(() -> createWalletService.execute(wallet))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Já existe uma carteira com esse nome para esse usuário.");

        verify(walletRepositoryPort).save(any(Wallet.class));
    }
}
