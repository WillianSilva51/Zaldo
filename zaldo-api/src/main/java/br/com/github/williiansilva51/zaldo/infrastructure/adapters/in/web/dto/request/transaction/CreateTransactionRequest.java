package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        @Schema(description = "description of transaction", example = "Salary")
        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @Schema(description = "amount of transaction", example = "1000.00")
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount,

        @Schema(description = "type of transaction", example = "INCOME")
        @NotNull(message = "O tipo é obrigatório (INCOME ou EXPENSE)")
        TransactionType type,

        @Schema(description = "date of transaction", example = "2023-01-01")
        LocalDate date,

        @Schema(description = "wallet id", example = "1")
        @NotNull(message = "O ID da carteira é obrigatório")
        Long walletId
) {
}
