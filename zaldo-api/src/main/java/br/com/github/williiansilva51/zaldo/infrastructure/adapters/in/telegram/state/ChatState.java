package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state;

public enum ChatState {
    IDLE, //Livre

    //Fluxo de Login
    WAITING_LOGIN_EMAIL,
    WAITING_LOGIN_PASSWORD,

    //Fluxo de Cadastro de Carteira
    WAITING_WALLET_NAME,
    WAITING_WALLET_DESCRIPTION,

    //Fluxo de Cadastro de Transação
    WAITING_TRANSACTION_AMOUNT,
    WAITING_TRANSACTION_DESCRIPTION,
    WAITING_TRANSACTION_DATE
}
