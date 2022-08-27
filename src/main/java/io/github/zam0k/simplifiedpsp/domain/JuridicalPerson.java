package io.github.zam0k.simplifiedpsp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "juridical_person")
public class JuridicalPerson extends Person {
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
}
