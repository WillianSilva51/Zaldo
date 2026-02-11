package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

public record FieldErrorResponse(
        String field,
        String message
) {
}
