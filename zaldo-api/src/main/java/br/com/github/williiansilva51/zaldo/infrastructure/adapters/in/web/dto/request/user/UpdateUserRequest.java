package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        @Schema(description = "name of user", example = "John Lennon")
        String name,

        @Schema(description = "email of user", example = "johnlennon@email.com")
        @Email(message = "O email deve ser válido")
        String email,

        @Schema(description = "password of user", example = "beatles123")
        String password) {
}
