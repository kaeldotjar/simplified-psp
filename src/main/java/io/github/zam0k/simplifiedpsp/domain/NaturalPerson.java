package io.github.zam0k.simplifiedpsp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "natural_person")
@Setter @Getter @EqualsAndHashCode
@NoArgsConstructor
public final class NaturalPerson implements IPayer, IPayee {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Column(nullable = false)
    private BigDecimal balance;

    @JsonIgnore
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
