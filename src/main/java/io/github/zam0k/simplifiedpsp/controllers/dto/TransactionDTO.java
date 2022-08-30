package io.github.zam0k.simplifiedpsp.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class TransactionDTO {
    private Long payer;
    private Long payee;
    private BigDecimal value;
}
