package io.github.zam0k.simplifiedpsp.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "juridical_person")
@Getter @Setter @EqualsAndHashCode
@NoArgsConstructor
public class JuridicalPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Column(nullable = false)
    private BigDecimal balance;
//    @OneToMany(mappedBy="payer")
//    private List<Transaction> payments;
//    @OneToMany(mappedBy="payee")
//    private List<Transaction> receipts;

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

}
