package br.com.github.williiansilva51.zaldo.core.domain;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private Long id;
    private BigDecimal amount;
    private String description;
    private TransactionType type;
    private LocalDate date;
    private Wallet wallet;

    public void validateState() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("O valor da transação deve ser positivo.");
        }

        if (date == null) {
            date = LocalDate.now();
        }
    }

    public void update(Transaction newInfo) {
        if (newInfo.getDescription() != null) {
            description = newInfo.getDescription();
        }
        if (newInfo.getType() != null) {
            type = newInfo.getType();
        }
        if (newInfo.getDate() != null) {
            date = newInfo.getDate();
        }
        if (newInfo.getAmount() != null) {
            amount = newInfo.getAmount();
        }

        validateState();
    }

    public void setWallet(Wallet newWallet) {
        if (newWallet != null) {
            wallet = newWallet;
        }
    }

    public boolean isPositive() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isIncome() {
        return TransactionType.INCOME.equals(type);
    }
}