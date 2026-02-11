package br.com.github.williiansilva51.zaldo.application.ports.in.wallet;

public interface DeleteWalletByIdUseCase {
    void execute(Long id, String authenticatedUserId);
}
