package io.github.zam0k.simplifiedpsp.controllers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
public class JuridicalPersonDTO {
    @CNPJ(message = "Invalid cnpj format")
    @NotBlank(message = "Cnpj cannot be empty")
    private String cnpj;
    @NotBlank(message = "Full Name cannot be empty")
    private String fullName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotNull(message = "Balance cannot be null")
    private BigDecimal balance;
    @NotBlank(message = "Juridical Person must have at least one owner")
    private List<NaturalPersonDTO> owners;

}
