package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.wallet;

import jakarta.validation.constraints.NotBlank;

public record CreateWalletRequest(
        @NotBlank(message = "O nome da carteira é obrigatório")
        String name,

        String description,

        @NotBlank(message = "O ID do usuário é obrigatório")
        String userId
) {
}
