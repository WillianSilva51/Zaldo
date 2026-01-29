package br.com.github.williiansilva51.zaldo.core.domain;

import java.util.List;

public record Paginated<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage
) {
}
