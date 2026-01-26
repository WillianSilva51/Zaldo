package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTransactionRequest(
        String description,

        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount,

        TransactionType type,

        LocalDate date) {
}
