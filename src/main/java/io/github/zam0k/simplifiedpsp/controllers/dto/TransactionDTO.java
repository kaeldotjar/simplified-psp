package io.github.zam0k.simplifiedpsp.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter @Setter
public class TransactionDTO {
    @NotNull(message = "Payer must not be null")
    private Long payer;
    @NotNull(message = "Payee must not be null")
    private Long payee;
    @NotNull(message = "Value must not be null")
    @DecimalMin(value = "0.01", message = "Minimum value must be 0.01")
    private BigDecimal value;
}
