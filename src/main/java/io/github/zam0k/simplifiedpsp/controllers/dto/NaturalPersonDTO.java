package io.github.zam0k.simplifiedpsp.controllers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter @Setter @ToString
public class NaturalPersonDTO {
    private Long id;
    private String cpf;
    private String fullName;
    private String email;
    private String password;
    private BigDecimal balance;
}
