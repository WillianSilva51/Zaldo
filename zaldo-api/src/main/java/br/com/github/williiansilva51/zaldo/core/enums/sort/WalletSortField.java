package br.com.github.williiansilva51.zaldo.core.enums.sort;

public enum WalletSortField implements SortField {
    name("name"),
    description("description"),
    createdAt("createdAt");

    private final String field;

    WalletSortField(String field) {
        this.field = field;
    }

    @Override
    public String field() {
        return field;
    }
}
