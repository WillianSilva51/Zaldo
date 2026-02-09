package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateTransactionRequest(
        @Schema(description = "description of transaction", example = "Salary")
        String description,

        @Schema(description = "amount of transaction", example = "1000.00")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount,

        @Schema(description = "type of transaction", example = "INCOME")
        TransactionType type,

        @Schema(description = "date of transaction", example = "2023-01-01")
        LocalDate date) {
}
