package io.github.zam0k.simplifiedpsp.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

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
@JsonPropertyOrder({"id", "fullName", "cpf", "email", "password", "balance"})
public class CommonUserDTO extends RepresentationModel<CommonUserDTO> {

    @JsonProperty("id")
    @Null(message = "Id must be null")
    private UUID key;
    @NotBlank(message = "Full Name cannot be empty")
    private String fullName;
    @CPF(message = "Invalid cpf format")
    @NotBlank(message = "Cpf cannot be empty")
    private String cpf;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @JsonProperty(access = WRITE_ONLY)
    private String password;
    @NotNull(message = "Balance cannot be null")
    private BigDecimal balance;
}
