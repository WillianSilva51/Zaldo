package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(Long id,
                                  String description,
                                  BigDecimal amount,
                                  TransactionType type,
                                  LocalDate date
) {
}
