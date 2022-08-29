package io.github.zam0k.simplifiedpsp.controllers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @ToString
public class JuridicalPersonDTO {
    private String cnpj;
    private String fullName;
    private String email;
    private String password;
    private BigDecimal balance;
    private List<NaturalPersonDTO> owners;

}
