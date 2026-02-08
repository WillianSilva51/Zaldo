package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state;


import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class FlowContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private ChatState chatState = ChatState.IDLE;

    private String authenticatedUserId;
    private String tempEmail;

    // Dados Temporários (Carteira e Transação)
    private Long tempUserId;
    private String tempWalletName;
    private String tempWalletDescription;


    private Long tempWalletId;
    private BigDecimal tempAmount;
    private String tempTransactionDescription;
    private Date tempDate;

    public void clear() {
        chatState = ChatState.IDLE;
        tempEmail = null;
        tempUserId = null;
        tempWalletName = null;
        tempWalletDescription = null;
        tempWalletId = null;
        tempAmount = null;
        tempTransactionDescription = null;
        tempDate = null;
    }
}
