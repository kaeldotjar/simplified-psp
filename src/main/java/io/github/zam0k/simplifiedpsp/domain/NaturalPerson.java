package io.github.zam0k.simplifiedpsp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "natural_person")
@Data
@NoArgsConstructor
public class NaturalPerson extends Person {
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
}
