package io.github.zam0k.simplifiedpsp.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    @Null(message = "Id must be null")
    private Long id;
    @NotNull(message = "Payer must not be null")
    private Long payer;
    @NotNull(message = "Payee must not be null")
    private Long payee;
    @NotNull(message = "Value must not be null")
    @DecimalMin(value = "0.01", message = "Minimum value must be 0.01")
    private BigDecimal value;
    @Null(message = "Timestamp must be null")
    private LocalDateTime timestamp;
}
