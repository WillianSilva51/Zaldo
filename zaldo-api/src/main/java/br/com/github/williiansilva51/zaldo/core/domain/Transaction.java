package br.com.github.williiansilva51.zaldo.core.domain;

import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.core.exceptions.DomainValidationException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity()
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private LocalDate date;

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

    public boolean isPositive() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isIncome() {
        return TransactionType.INCOME.equals(type);
    }
}