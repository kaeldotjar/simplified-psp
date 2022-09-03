package io.github.zam0k.simplifiedpsp.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommonUserDTO {
    @Null(message = "Id must be null")
    private Long id;
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
