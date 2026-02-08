package br.com.github.williiansilva51.zaldo.core.domain;

import java.util.List;

public record Paginated<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage
) {
    public boolean hasNext() {
        return currentPage < (totalPages - 1);
    }

    public boolean hasPrevious() {
        return currentPage > 0;
    }

    public int nextPage() {
        return currentPage + 1;
    }

    public int previousPage() {
        return currentPage - 1;
    }
}
