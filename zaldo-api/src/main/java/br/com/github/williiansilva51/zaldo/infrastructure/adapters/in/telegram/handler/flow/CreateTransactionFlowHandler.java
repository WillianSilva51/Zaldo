package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.handler.flow;

import br.com.github.williiansilva51.zaldo.application.ports.in.transaction.CreateTransactionUseCase;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.ChatState;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.FlowContext;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state.UserSessionManager;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils.MenuUtils;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.web.dto.request.transaction.CreateTransactionRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateTransactionFlowHandler implements FlowHandler {
    private final UserSessionManager sessionManager;
    private final CreateTransactionUseCase createTransactionUseCase;
    private final Validator validator;

    @Override
    public boolean canHandle(ChatState chatState) {
        return chatState.name().startsWith("WAITING_TRANSACTION");
    }

    @Override
    public SendMessage handleInput(Long chatId, String text, String userId) {
        FlowContext context = sessionManager.get(chatId);

        if (context.getChatState().equals(ChatState.WAITING_TRANSACTION_DESCRIPTION)) {
            return processTransactionDescriptionInput(chatId, text, context);
        } else if (context.getChatState().equals(ChatState.WAITING_TRANSACTION_AMOUNT)) {
            return processTransactionAmountInput(chatId, text, context);
        } else if (context.getChatState().equals(ChatState.WAITING_TRANSACTION_DATE)) {
            return processTransactionDateInput(chatId, text, context);
        }

        return null;
    }

    private SendMessage processTransactionDescriptionInput(Long chatId, String text, FlowContext context) {
        Set<ConstraintViolation<CreateTransactionRequest>> violations = getConstraintViolations("description", text);

        if (!violations.isEmpty()) {
            return messageError(chatId, violations);
        }

        context.setChatState(ChatState.WAITING_TRANSACTION_AMOUNT);
        context.setTempTransactionDescription(text);

        sessionManager.save(chatId, context);

        return SendMessage.builder()
                .chatId(chatId)
                .text("📧 Descrição salva! Agora digite o valor da <b>transação</b>: \n" +
                        "(Exemplo: 100.00 ou 100,00)")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("SEL_WALLET:" + context.getTempWalletId())))
                        .build())
                .parseMode("HTML")
                .build();
    }

    private SendMessage messageError(Long chatId, Set<ConstraintViolation<CreateTransactionRequest>> violations) {
        String msgError = violations.iterator().next().getMessage();

        return SendMessage.builder()
                .chatId(chatId)
                .text("❌ " + msgError + ". Tente novamente:")
                .build();
    }

    private <T> Set<ConstraintViolation<CreateTransactionRequest>> getConstraintViolations(String columns, T text) {
        return validator
                .validateValue(CreateTransactionRequest.class, columns, text);
    }

    private SendMessage processTransactionAmountInput(Long chatId, String text, FlowContext context) {
        BigDecimal amount;

        try {
            amount = new BigDecimal(text.replace(",", "."));
        } catch (NumberFormatException e) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("❌ Valor inválido. Digite um número válido, exemplo: 10.50 ou 10,50")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("SEL_WALLET:" + context.getTempWalletId())))
                            .build())
                    .build();
        }

        Set<ConstraintViolation<CreateTransactionRequest>> violations = getConstraintViolations("amount", amount);

        if (!violations.isEmpty()) {
            return messageError(chatId, violations);
        }


        context.setChatState(ChatState.WAITING_TRANSACTION_DATE);
        context.setTempAmount(amount);

        sessionManager.save(chatId, context);

        return SendMessage.builder()
                .chatId(chatId)
                .text("📧 Valor salvo! Agora digite a data da <b>transação</b>(YYYY/MM/DD ou YYYY-MM-DD) (Opcional):")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("SEL_WALLET:" + context.getTempWalletId())))
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createButton("Pular data", "BTN_SKIP_DATE")))
                        .build())
                .parseMode("HTML")
                .build();
    }

    private SendMessage processTransactionDateInput(Long chatId, String text, FlowContext context) {
        LocalDate date = null;

        if (text != null) {
            try {
                date = LocalDate.parse(text.replace("/", "-"));
            } catch (DateTimeException e) {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text("❌ Data inválida. Digite uma data válida, exemplo: 2025/05/10 ou 2025-05-10")
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("SEL_WALLET:" + context.getTempWalletId())))
                                .keyboardRow(new InlineKeyboardRow(MenuUtils.createButton("Pular data", "BTN_SKIP_DATE")))
                                .build())
                        .build();
            }
        }

        Long walletId = context.getTempWalletId();
        Wallet wallet = Wallet.builder().id(walletId).build();

        Transaction transaction = Transaction.builder()
                .amount(context.getTempAmount())
                .description(context.getTempTransactionDescription())
                .type(context.getTempTransactionType())
                .date(date)
                .wallet(wallet)
                .build();

        createTransactionUseCase.execute(transaction);
        sessionManager.clearSession(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("✅ <b>Sucesso!</b> Transação criada.\n\nO que deseja fazer agora?")
                .parseMode("HTML")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(new InlineKeyboardRow(MenuUtils.createBackButton("SEL_WALLET:" + walletId))).build())
                .build();
    }
}
