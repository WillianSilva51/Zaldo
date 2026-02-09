package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "id of user")
        String id,

        @Schema(description = "name of user")
        String name,

        @Schema(description = "email of user")
        String email) {
}
