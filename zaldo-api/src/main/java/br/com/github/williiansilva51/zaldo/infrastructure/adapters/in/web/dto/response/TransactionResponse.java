package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        @Schema(description = "id of transaction")
        Long id,

        @Schema(description = "description of transaction")
        String description,

        @Schema(description = "amount of transaction")
        BigDecimal amount,

        @Schema(description = "type of transaction")
        TransactionType type,

        @Schema(description = "date of transaction")
        LocalDate date
) {
}
