package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> data,
        long totalItems,
        int totalPages,
        int currentPage
) {
}