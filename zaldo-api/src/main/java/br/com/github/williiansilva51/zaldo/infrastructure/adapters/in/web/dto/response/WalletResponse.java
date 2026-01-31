package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

import java.time.LocalDateTime;

public record WalletResponse(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        String userId,
        String userName
) {
}
