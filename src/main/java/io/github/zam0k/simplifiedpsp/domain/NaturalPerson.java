package io.github.zam0k.simplifiedpsp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
@Table(name = "natural_person")
@Setter @Getter @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public final class NaturalPerson implements IPayer, IPayee {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;
    @Column(nullable = false, unique = true, length = 14)
    @CPF(message = "Invalid cpf format")
    private String cpf;
    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email format")
    private String email;
    @JsonProperty(access = WRITE_ONLY)
    private String password;
    @Column(nullable = false)
    private BigDecimal balance;

    @JsonProperty(access = WRITE_ONLY)
    @ManyToMany
    @JoinTable(name = "belongs_to",
    joinColumns = @JoinColumn(name = "natural_person_id"),
    inverseJoinColumns = @JoinColumn(name = "juridical_person_id"))
    private List<JuridicalPerson> shops;

    @Override
    public void receiveValue(BigDecimal value) {
        setBalance(getBalance().add(value));
    }

    @Override
    public void removeValue(BigDecimal value) {
        setBalance(getBalance().subtract(value));
    }
}
