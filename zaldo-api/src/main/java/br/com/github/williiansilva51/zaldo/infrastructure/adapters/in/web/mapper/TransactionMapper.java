package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.mapper;

import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction.CreateTransactionRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction.UpdateTransactionRequest;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.response.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper implements Mapper<Transaction, CreateTransactionRequest, TransactionResponse, UpdateTransactionRequest> {
    @Override
    public Transaction toDomain(CreateTransactionRequest request) {
        return Transaction.builder()
                .description(request.description())
                .amount(request.amount())
                .type(request.type())
                .date(request.date())
                .wallet(Wallet.builder().id(request.walletId()).build())
                .build();
    }

    @Override
    public Transaction toDomainByUpdate(UpdateTransactionRequest request) {
        return Transaction.builder()
                .description(request.description())
                .amount(request.amount())
                .type(request.type())
                .date(request.date())
                .build();
    }

    @Override
    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDate());
    }
}
