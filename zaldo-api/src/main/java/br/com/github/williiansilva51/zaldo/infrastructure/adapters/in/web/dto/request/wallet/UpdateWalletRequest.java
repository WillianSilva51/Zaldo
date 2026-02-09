package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateWalletRequest(
        @Schema(description = "name of wallet", example = "Wallet 1")
        String name,

        @Schema(description = "description of wallet", example = "Wallet for salary")
        String description
) {
}
