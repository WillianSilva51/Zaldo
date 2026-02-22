package br.com.github.williiansilva51.zaldo.application.service.wallet;

import br.com.github.williiansilva51.zaldo.application.ports.in.wallet.FindWalletByIdUseCase;
import br.com.github.williiansilva51.zaldo.application.ports.out.WalletRepositoryPort;
import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.exceptions.BusinessRuleException;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateWalletServiceTest {
    @Mock
    private FindWalletByIdUseCase findWalletByIdUseCase;

    @Mock
    private WalletRepositoryPort walletRepositoryPort;

    @InjectMocks
    private UpdateWalletService updateWalletService;

    private Wallet existingWallet;
    private Wallet updateRequest;
    private Long walletId;
    private String userId;

    @BeforeEach
    void setUp() {
        existingWallet = createValidWallet("wallet1", "description1");
        updateRequest = createValidWallet("newWallet", "newDescription");
        walletId = existingWallet.getId();
        userId = existingWallet.getUser().getId();
    }

    private Wallet createValidWallet(String name, String description) {
        return Wallet.builder()
                .id(1L)
                .name(name)
                .description(description)
                .createdAt(LocalDateTime.now())
                .user(User.builder().id("userId1").build())
                .build();
    }

    @Test
    @DisplayName("Deve atualizar a carteira, se ela pertencer ao usuário solicitado")
    void shouldUpdateWalletSuccessfully() {
        when(findWalletByIdUseCase.execute(walletId)).thenReturn(existingWallet);
        when(walletRepositoryPort.existsByUserIdAndName(userId, updateRequest.getName())).thenReturn(false);
        when(walletRepositoryPort.save(any(Wallet.class)))
                .thenAnswer(i -> i.getArgument(0));

        updateWalletService.execute(walletId, userId, updateRequest);

        verify(findWalletByIdUseCase).execute(walletId);
        verify(walletRepositoryPort).existsByUserIdAndName(userId, updateRequest.getName());

        verify(walletRepositoryPort).save(argThat(savedWallet -> {
            assertThat(savedWallet.getName()).isEqualTo(updateRequest.getName());
            assertThat(savedWallet.getDescription()).isEqualTo(updateRequest.getDescription());
            assertThat(savedWallet.getUser()).isEqualTo(existingWallet.getUser());
            assertThat(savedWallet.getCreatedAt()).isEqualTo(existingWallet.getCreatedAt());
            return true;
        }));

        verifyNoMoreInteractions(findWalletByIdUseCase, walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'DomainValidationException' se a carteira não pertencer ao usuário solicitado")
    void shouldThrowDomainValidationExceptionWhenWalletDoesNotBelongToUser() {
        String userIdFalse = "userIdFalse";

        when(findWalletByIdUseCase.execute(walletId)).thenReturn(existingWallet);

        assertThatThrownBy(() -> updateWalletService.execute(walletId, userIdFalse, updateRequest))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Você não tem permissão para atualizar esta carteira.");

        verify(findWalletByIdUseCase).execute(walletId);
        verify(walletRepositoryPort, never()).existsByUserIdAndName(userIdFalse, updateRequest.getName());
        verify(walletRepositoryPort, never()).save(any(Wallet.class));

        verifyNoMoreInteractions(findWalletByIdUseCase, walletRepositoryPort);
    }

    @Test
    @DisplayName("Deve lançar 'BusinessRuleException' se a atualização da carteira mudar o nome da carteira e for igual a outra carteira do usuário")
    void shouldThrowBusinessRuleExceptionWhenWalletNameChanges() {
        when(findWalletByIdUseCase.execute(walletId)).thenReturn(existingWallet);
        when(walletRepositoryPort.existsByUserIdAndName(userId, updateRequest.getName())).thenReturn(true);

        assertThatThrownBy(() -> updateWalletService.execute(walletId, userId, updateRequest))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage(String.format(
                        "Já existe uma carteira com o nome '%s' para esse usuário.",
                        updateRequest.getName()
                ));

        verify(findWalletByIdUseCase).execute(walletId);
        verify(walletRepositoryPort).existsByUserIdAndName(userId, updateRequest.getName());
        verify(walletRepositoryPort, never()).save(any(Wallet.class));

        verifyNoMoreInteractions(findWalletByIdUseCase, walletRepositoryPort);
    }

    @Test
    @DisplayName("Não deve consultar o banco se o nome da carteira for o mesmo da existente")
    void shouldNotCheckDatabaseWhenNameIsUnchanged() {
        updateRequest.setName(existingWallet.getName());

        when(findWalletByIdUseCase.execute(walletId)).thenReturn(existingWallet);
        when(walletRepositoryPort.save(any(Wallet.class))).thenAnswer(i -> i.getArgument(0));

        updateWalletService.execute(walletId, userId, updateRequest);

        verify(walletRepositoryPort, never()).existsByUserIdAndName(anyString(), anyString());
        verify(walletRepositoryPort).save(existingWallet);
    }

    @Test
    @DisplayName("Deve lançar BusinessRuleException quando o banco reportar violação de integridade no save")
    void shouldThrowBusinessRuleExceptionWhenDatabaseViolationOccurs() {
        when(findWalletByIdUseCase.execute(walletId)).thenReturn(existingWallet);
        when(walletRepositoryPort.save(any(Wallet.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Constraint violation"));

        assertThatThrownBy(() -> updateWalletService.execute(walletId, userId, updateRequest))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Já existe uma carteira com esse nome para esse usuário.");

        verify(findWalletByIdUseCase).execute(walletId);
        verify(walletRepositoryPort).save(any(Wallet.class));

    }
}
