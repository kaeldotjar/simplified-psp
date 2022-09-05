package io.github.zam0k.simplifiedpsp.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "payer", "payee", "value", "timestamp"})
@Relation(collectionRelation = "transactions", itemRelation = "transaction")
public class TransactionDTO extends RepresentationModel<TransactionDTO> {
    @JsonProperty("id")
    @Null(message = "Id must be null")
    private UUID key;
    @NotNull(message = "Payer must not be null")
    private UUID payer;
    @NotNull(message = "Payee must not be null")
    private UUID payee;
    @NotNull(message = "Value must not be null")
    @DecimalMin(value = "0.01", message = "Minimum value must be 0.01")
    private BigDecimal value;
    @Null(message = "Timestamp must be null")
    private LocalDateTime timestamp;
}
