package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

public class MenuUtils {
    public static InlineKeyboardMarkup createMainKeyboard() {
        // Linha 1: Ações Rápidas
        InlineKeyboardButton btnExpense = createButton("📉 Nova Despesa", "BTN_NEW_EXPENSE");
        InlineKeyboardButton btnIncome = createButton("📈 Nova Receita", "BTN_NEW_INCOME");

        InlineKeyboardRow row1 = new InlineKeyboardRow(btnExpense, btnIncome);

        // Linha 2: Consultas
        InlineKeyboardButton btnWallets = createButton("💰 Carteiras", "BTN_WALLETS");
        InlineKeyboardButton btnStatement = createButton("📊 Extrato", "BTN_STATEMENT");
        InlineKeyboardRow row2 = new InlineKeyboardRow(btnWallets, btnStatement);

        // Linha 3: Configurações / Sistema
        InlineKeyboardButton btnWeb = createButton("🔑 Acesso Web / Login", "BTN_LOGIN");
        InlineKeyboardRow row3 = new InlineKeyboardRow(btnWeb);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .keyboardRow(row3)
                .build();
    }

    private static InlineKeyboardButton createButton(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}
