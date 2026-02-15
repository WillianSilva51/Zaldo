package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Transaction;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
import br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums.BotAction;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {
    public static InlineKeyboardMarkup createMainKeyboard() {
        InlineKeyboardButton btnWallets = createButton("\uD83D\uDCB0 Minhas Carteiras", BotAction.LIST_WALLETS.build(0));
        InlineKeyboardButton btnWeb = createButton("⚙\uFE0F Configurações / Acesso Web", BotAction.LOGIN.getActionName());

        InlineKeyboardRow row1 = new InlineKeyboardRow(btnWallets);
        InlineKeyboardRow row2 = new InlineKeyboardRow(btnWeb);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .build();
    }

    public static InlineKeyboardMarkup createWalletsKeyboard() {
        InlineKeyboardButton btnExpense = createButton("📉 Nova Despesa", BotAction.NEW_TRANSACTION.build("EXPENSE"));
        InlineKeyboardButton btnIncome = createButton("📈 Nova Receita", BotAction.NEW_TRANSACTION.build("INCOME"));
        InlineKeyboardButton btnStatement = createButton("📊 Extrato", BotAction.LIST_TRANSACTIONS.build(0));
        InlineKeyboardButton btnDeleteWallet = createButton("❌ Deletar Carteira", BotAction.CONFIRM_DELETE_WALLET.getActionName());
        InlineKeyboardButton btnReturn = createBackButton(BotAction.LIST_WALLETS.build(0));

        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(btnExpense, btnIncome))
                .keyboardRow(new InlineKeyboardRow(btnStatement, btnDeleteWallet))
                .keyboardRow(new InlineKeyboardRow(btnReturn))
                .build();
    }

    public static InlineKeyboardMarkup createTransactionsKeyboard() {
        InlineKeyboardButton btnUpdateTransaction = createButton("✏\uFE0F Editar Transação", BotAction.EDIT_TRANSACTION.getActionName());
        InlineKeyboardButton btnDeleteTransaction = createButton("❌ Deletar Transação", BotAction.CONFIRM_DELETE_TRANSACTION.getActionName());
        InlineKeyboardButton btnReturn = createBackButton(BotAction.LIST_TRANSACTIONS.build(0));

        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(btnUpdateTransaction, btnDeleteTransaction))
                .keyboardRow(new InlineKeyboardRow(btnReturn))
                .build();
    }

    public static InlineKeyboardMarkup createListWallets(Paginated<Wallet> wallets) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        List<Wallet> walletsList = wallets.content();

        for (Wallet wallet : walletsList) {
            String callbackData = BotAction.SELECT_WALLET.build(wallet.getId());

            InlineKeyboardButton button = createButton("\uD83D\uDCB3 " + wallet.getName(), callbackData);

            rows.add(new InlineKeyboardRow(button));
        }

        if (wallets.hasPrevious()) {
            rows.add(new InlineKeyboardRow(
                    createButton("⬅\uFE0F Anterior", BotAction.LIST_WALLETS.build(wallets.currentPage() - 1))
            ));
        }

        if (wallets.hasNext()) {
            rows.add(new InlineKeyboardRow(
                    createButton("➡\uFE0F Próxima", BotAction.LIST_WALLETS.build(wallets.currentPage() + 1))
            ));
        }


        rows.add(new InlineKeyboardRow(createButton("➕ Nova Carteira", BotAction.CREATE_WALLET.getActionName())));
        rows.add(new InlineKeyboardRow(createBackButton(BotAction.MAIN_MENU.getActionName())));

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public static InlineKeyboardMarkup createListTransactions(Paginated<Transaction> transactions) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        List<Transaction> transactionsList = transactions.content();

        for (Transaction transaction : transactionsList) {
            String callbackData = BotAction.SELECT_TRANSACTION.build(transaction.getId());

            String emoji = transaction.getType() == TransactionType.INCOME ? "\uD83D\uDFE2" : "\uD83D\uDD34";

            String text = String.format("%s %s - R$ %s",
                    emoji, transaction.getDescription(), transaction.getAmount());

            InlineKeyboardButton button = createButton(text, callbackData);

            rows.add(new InlineKeyboardRow(button));
        }


        if (transactions.hasPrevious()) {
            rows.add(new InlineKeyboardRow(
                    createButton("⬅\uFE0F Anterior", BotAction.LIST_TRANSACTIONS.build(transactions.currentPage() - 1))
            ));
        }

        if (transactions.hasNext()) {
            rows.add(new InlineKeyboardRow(
                    createButton("➡\uFE0F Próxima", BotAction.LIST_TRANSACTIONS.build(transactions.currentPage() + 1))
            ));
        }

        if (!transactionsList.isEmpty()) {
            rows.add(new InlineKeyboardRow(createBackButton(BotAction.SELECT_WALLET.build(transactionsList
                    .getFirst()
                    .getWallet()
                    .getId()))));
        }

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public static InlineKeyboardButton createButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }

    public static InlineKeyboardButton createBackButton(String callbackData) {
        return createButton("\uD83D\uDD19 Voltar", callbackData);
    }

    public static EditMessageText createErrorMessage(Long chatId, Integer messageId, String message) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(message)
                .replyMarkup(MenuUtils.createMainKeyboard())
                .parseMode("HTML")
                .build();
    }
}
