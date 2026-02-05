package br.com.github.williiansilva51.zaldo.core.enums.sort;

public enum TransactionSortField implements SortField {
    amount("amount"),
    description("description"),
    type("type"),
    date("date");

    private final String field;

    TransactionSortField(String field) {
        this.field = field;
    }

    @Override
    public String field() {
        return field;
    }
}
