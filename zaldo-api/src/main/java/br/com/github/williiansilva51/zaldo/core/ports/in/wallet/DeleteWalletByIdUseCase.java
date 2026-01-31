package br.com.github.williiansilva51.zaldo.core.ports.in.wallet;

public interface DeleteWalletByIdUseCase {
    void execute(Long id, String authenticatedUserId);
}
