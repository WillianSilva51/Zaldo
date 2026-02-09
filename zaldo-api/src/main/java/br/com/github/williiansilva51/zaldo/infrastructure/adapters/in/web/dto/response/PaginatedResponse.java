package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PaginatedResponse<T>(
        @Schema(description = "list of data")
        List<T> data,

        @Schema(description = "total items")
        long totalItems,

        @Schema(description = "total pages")
        int totalPages,

        @Schema(description = "current page")
        int currentPage
) {
}