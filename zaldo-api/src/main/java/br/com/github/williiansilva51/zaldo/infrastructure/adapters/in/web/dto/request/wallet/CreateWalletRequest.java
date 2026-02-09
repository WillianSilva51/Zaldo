package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateWalletRequest(
        @Schema(description = "name of wallet", example = "Wallet 1")
        @NotBlank(message = "O nome da carteira é obrigatório")
        String name,

        @Schema(description = "description of wallet", example = "Wallet for salary")
        String description,

        @Schema(description = "user ID", example = "1")
        @NotBlank(message = "O ID do usuário é obrigatório")
        String userId
) {
}
