package br.com.github.williiansilva51.zaldo.infrastructure.adapters.in.telegram.state;


import br.com.github.williiansilva51.zaldo.core.domain.User;
import br.com.github.williiansilva51.zaldo.core.enums.TransactionType;
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

    private User authenticatedUser;
    private String tempEmail;

    // Dados Temporários (Carteira e Transação)
    private Long tempUserId;
    private String tempWalletName;
    private String tempWalletDescription;


    private Long tempWalletId;
    private BigDecimal tempAmount;
    private String tempTransactionDescription;
    private TransactionType tempTransactionType;
    private Date tempDate;
}
