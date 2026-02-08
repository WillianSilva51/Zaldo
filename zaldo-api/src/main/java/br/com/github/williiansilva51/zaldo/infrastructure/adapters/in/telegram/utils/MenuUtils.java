package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils;

import br.com.github.williiansilva51.zaldo.core.domain.Paginated;
import br.com.github.williiansilva51.zaldo.core.domain.Wallet;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {
    public static InlineKeyboardMarkup createMainKeyboard() {
        InlineKeyboardButton btnWallets = createButton("\uD83D\uDCB0 Minhas Carteiras", "BTN_LIST_WALLETS");
        InlineKeyboardButton btnWeb = createButton("⚙\uFE0F Configurações / Acesso Web", "BTN_LOGIN");

        InlineKeyboardRow row1 = new InlineKeyboardRow(btnWallets);
        InlineKeyboardRow row2 = new InlineKeyboardRow(btnWeb);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .build();
    }

    public static InlineKeyboardMarkup createWalletsKeyboard() {
        InlineKeyboardButton btnExpense = createButton("📉 Nova Despesa", "BTN_NEW_EXPENSE");
        InlineKeyboardButton btnIncome = createButton("📈 Nova Receita", "BTN_NEW_INCOME");
        InlineKeyboardButton btnStatement = createButton("📊 Extrato", "BTN_STATEMENT");
        InlineKeyboardButton btnDeleteWallet = createButton("❌ Deletar Carteira", "BTN_DELETE_WALLET");
        InlineKeyboardButton btnReturn = createBackButton("BTN_LIST_WALLETS");

        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(btnExpense, btnIncome))
                .keyboardRow(new InlineKeyboardRow(btnStatement, btnDeleteWallet))
                .keyboardRow(new InlineKeyboardRow(btnReturn))
                .build();
    }


    public static InlineKeyboardMarkup createListWallets(Paginated<Wallet> wallets) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        List<Wallet> walletsList = wallets.content();

        for (Wallet wallet : walletsList) {
            String callbackData = "SEL_WALLET:" + wallet.getId();

            InlineKeyboardButton button = createButton("\uD83D\uDCB3 " + wallet.getName(), callbackData);

            rows.add(new InlineKeyboardRow(button));
        }

        if (wallets.hasPrevious()) {
            rows.add(new InlineKeyboardRow(
                    createButton("⬅\uFE0F Anterior", "WALLETS_PAGE:" + (wallets.currentPage() - 1))
            ));
        }

        if (wallets.hasNext()) {
            rows.add(new InlineKeyboardRow(
                    createButton("➡\uFE0F Próxima", "WALLETS_PAGE:" + (wallets.currentPage() + 1))
            ));
        }


        rows.add(new InlineKeyboardRow(createButton("➕ Nova Carteira", "BTN_CREATE_WALLET")));
        rows.add(new InlineKeyboardRow(createBackButton("BTN_MAIN_MENU")));

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
}
