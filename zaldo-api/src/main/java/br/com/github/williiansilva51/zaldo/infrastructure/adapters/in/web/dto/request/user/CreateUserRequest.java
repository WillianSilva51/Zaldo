package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @Schema(description = "name of user", example = "John Lennon")
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @Schema(description = "email of user", example = "johnlennon@email.com")
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email deve ser válido")
        String email,

        @Schema(description = "password of user", example = "beatles123")
        @NotBlank(message = "A senha é obrigatória")
        String password) {
}
