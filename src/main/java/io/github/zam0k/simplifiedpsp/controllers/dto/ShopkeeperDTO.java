package io.github.zam0k.simplifiedpsp.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "fullName", "cnpj", "email", "password", "balance"})
@Relation(collectionRelation = "shopkeepers", itemRelation = "shopkeeper")
public class ShopkeeperDTO extends RepresentationModel<ShopkeeperDTO> {
  @JsonProperty("id")
  @Null(message = "Id must be null")
  private UUID key;

  @NotBlank(message = "Full Name cannot be empty")
  private String fullName;

  @CNPJ(message = "Invalid cnpj format")
  @NotBlank(message = "Cnpj cannot be empty")
  private String cnpj;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email cannot be empty")
  private String email;

  @NotBlank(message = "Password cannot be empty")
  @JsonProperty(access = WRITE_ONLY)
  private String password;

  @NotNull(message = "Balance cannot be null")
  private BigDecimal balance;
}
