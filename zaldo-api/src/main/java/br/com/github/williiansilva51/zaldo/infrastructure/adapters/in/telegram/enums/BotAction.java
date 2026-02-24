package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BotAction {
    MAIN_MENU("BTN_MAIN_MENU"),

    // --- Login / Usuário ---
    CONFIG("BTN_CONFIG"),
    MANEGE_USER("BTN_MANEGE_USER"),
    EDIT_USER("BTN_EDIT_USER"),
    LOGIN("BTN_LOGIN"),
    CONFIRM_DELETE_USER("BTN_CONFIRM_DELETE_USER"),
    DELETE_USER("BTN_DELETE_USER"),

    // --- Carteiras (Wallets) ---
    LIST_WALLETS("BTN_LIST_WALLETS"),
    CREATE_WALLET("BTN_CREATE_WALLET"),
    SELECT_WALLET("SEL_WALLET"),      // Ex: SEL_WALLET:1
    DELETE_WALLET("BTN_DELETE_WALLET"),  // Ex: BTN_DEL_WALLET:1
    CONFIRM_DELETE_WALLET("BTN_CONFIRM_DELETE_WALLET"),
    SKIP_DESCRIPTION_WALLET("BTN_SKIP_DESCRIPTION"),

    // --- Transações ---
    NEW_TRANSACTION("BTN_NEW_TRANSACTION"),
    LIST_TRANSACTIONS("BTN_LIST_TRANSACTIONS"),
    SELECT_TRANSACTION("SEL_TRANSACTION"),
    EDIT_TRANSACTION("BTN_EDIT_TRANSACTION"),
    EDIT_TRANSACTION_FIELD("EDIT_TRANSACTION_FIELD"),
    CONFIRM_DELETE_TRANSACTION("BTN_CONFIRM_DELETE_TRANSACTION"),
    DELETE_TRANSACTION("BTN_DELETE_TRANSACTION"),
    SKIP_DATE_TRANSACTION("BTN_SKIP_DATE"),

    MANEGE_CATEGORY("BTN_MANEGE_CATEGORY");

    private final String actionName;

    public static String extractArg(String data) {
        if (data.contains(":")) {
            return data.substring(data.indexOf(":") + 1);
        }
        return "";
    }

    @Override
    public String toString() {
        return actionName;
    }

    public String build(Object... args) {
        if (args == null || args.length == 0) {
            return actionName;
        }

        StringBuilder builder = new StringBuilder(actionName);
        for (Object arg : args) {
            builder.append(":").append(arg);
        }

        return builder.toString();
    }
}
