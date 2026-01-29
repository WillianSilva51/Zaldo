package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

public record WalletResponse(
        Long id,
        String name,
        String description,
        String userId,
        String userName
) {
}
