package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.user;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        String name,

        @Email(message = "O email deve ser válido")
        String email,

        String password) {
}
