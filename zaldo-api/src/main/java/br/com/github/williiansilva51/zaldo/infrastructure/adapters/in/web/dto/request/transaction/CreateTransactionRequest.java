package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount,

        @NotNull(message = "O tipo é obrigatório (INCOME ou EXPENSE)")
        TransactionType type,

        LocalDate date,

        @NotNull(message = "O ID da carteira é obrigatório")
        Long walletId
) {
}
