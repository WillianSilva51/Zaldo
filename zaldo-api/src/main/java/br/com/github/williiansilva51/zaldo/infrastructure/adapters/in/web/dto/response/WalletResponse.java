package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record WalletResponse(
        @Schema(description = "id of wallet")
        Long id,

        @Schema(description = "name of wallet")
        String name,

        @Schema(description = "description of wallet")
        String description,

        @Schema(description = "date of creation")
        LocalDateTime createdAt,

        @Schema(description = "user ID")
        String userId,

        @Schema(description = "user name")
        String userName
) {
}
