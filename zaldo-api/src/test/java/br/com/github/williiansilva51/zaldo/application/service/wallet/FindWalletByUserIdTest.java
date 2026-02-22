package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.out.UserRepositoryPort;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.DirectionOrder;
import br.com.github.williiansilva51.zaldo.core.enums.sort.WalletSortField;
import br.com.github.williiansilva51.zaldo.core.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindWalletByUserIdTest {
    private final String userId = "userId1";

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private WalletRepositoryPort walletRepositoryPort;

    @InjectMocks
    private FindWalletByUserIdService findWalletByUserIdService;
    private Paginated<Wallet> walletPaginated;

    @BeforeEach
    void setUp() {
        List<Wallet> walletList = List.of(
                createValidWallet(1L, "wallet1"),
                createValidWallet(2L, "wallet2"),
                createValidWallet(3L, "wallet3")
        );

        walletPaginated = new Paginated<>(walletList, 3, 1, 0);
    }

    private Wallet createValidWallet(Long id, String name) {
        return Wallet.builder()
                .id(id)
                .name(name)
                .description("money")
                .user(User.builder().id(userId).build())
                .build();
    }

    @Test
    @DisplayName("Deve retornar carteiras paginadas para um usuário específico")
    void shouldReturnPaginatedWalletsForUser() {
        int page = 0;
        int size = 5;
        WalletSortField walletSortField = WalletSortField.createdAt;
        DirectionOrder directionOrder = DirectionOrder.DESC;

        when(userRepositoryPort.existsById(userId)).thenReturn(true);
        when(walletRepositoryPort.findByUserId(userId, page, size, walletSortField, directionOrder))
                .thenReturn(walletPaginated);

        Paginated<Wallet> result = findWalletByUserIdService.execute(userId, page, size, walletSortField, directionOrder);

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(3).containsAll(walletPaginated.content());
        assertThat(result.totalElements()).isEqualTo(walletPaginated.totalElements());
        assertThat(result.totalPages()).isEqualTo(walletPaginated.totalPages());
        assertThat(result.currentPage()).isEqualTo(walletPaginated.currentPage());

        verify(userRepositoryPort).existsById(userId);
        verify(walletRepositoryPort).findByUserId(userId, page, size, walletSortField, directionOrder);
        verifyNoMoreInteractions(walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando o usuário não possuir carteiras")
    void shouldReturnEmptyPageWhenUserHasNoWallets() {
        int page = 0;
        int size = 5;
        WalletSortField walletSortField = WalletSortField.createdAt;
        DirectionOrder directionOrder = DirectionOrder.DESC;
        Paginated<Wallet> emptyPaginatedWallets = new Paginated<>(List.of(), 0, 0, 0);

        when(userRepositoryPort.existsById(userId)).thenReturn(true);
        when(walletRepositoryPort.findByUserId(userId, page, size, walletSortField, directionOrder))
                .thenReturn(emptyPaginatedWallets);

        Paginated<Wallet> result = findWalletByUserIdService.execute(userId, page, size, walletSortField, directionOrder);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
        assertThat(result.totalPages()).isZero();
        assertThat(result.currentPage()).isZero();

        verify(userRepositoryPort).existsById(userId);
        verify(walletRepositoryPort).findByUserId(eq(userId), anyInt(), anyInt(), any(WalletSortField.class), any(DirectionOrder.class));
        verifyNoMoreInteractions(walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'ResourceNotFoundException quando não existir o usuário'")
    void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepositoryPort.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> findWalletByUserIdService
                .execute(userId, 0, 10, WalletSortField.name, DirectionOrder.ASC))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado com ID: " + userId);

        verify(userRepositoryPort).existsById(userId);
        verify(walletRepositoryPort, never()).findByUserId(anyString(), anyInt(), anyInt(), any(WalletSortField.class), any(DirectionOrder.class));
        verifyNoMoreInteractions(userRepositoryPort, walletRepositoryPort);
    }
}
