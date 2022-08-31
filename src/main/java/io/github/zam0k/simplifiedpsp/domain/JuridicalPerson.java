package io.github.zam0k.simplifiedpsp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "juridical_person")
@Getter @Setter @EqualsAndHashCode
@NoArgsConstructor
public final class JuridicalPerson implements IPayee {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;
    @Column(nullable = false, unique = true, length = 14)
    @CNPJ(message = "Invalid cnpj format")
    private String cnpj;
    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToMany(mappedBy = "shops")
    private List<NaturalPerson> owners;

    public void addOwner(NaturalPerson person) {
        owners.add(person);
        person.getShops().add(this);
    }

    public void addOwners(List<NaturalPerson> person) {
        owners.addAll(person);
        person.forEach(p -> p.getShops().add(this));
    }

    @Override
    public void receiveValue(BigDecimal value) {
        setBalance(getBalance().add(value));
    }
}
