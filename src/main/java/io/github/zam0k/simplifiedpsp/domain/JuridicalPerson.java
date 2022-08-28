package io.github.zam0k.simplifiedpsp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "juridical_person")
@DiscriminatorValue("J")
@Data
@NoArgsConstructor
public class JuridicalPerson extends Person {
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
}
