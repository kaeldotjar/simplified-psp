package io.github.zam0k.simplifiedpsp.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("P")
@NoArgsConstructor
@Data
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private BigDecimal balance;
    @Column(insertable = false, updatable = false)
    private String type;
    @OneToMany(mappedBy="payer")
    private List<Transaction> payments;
    @OneToMany(mappedBy="payee")
    private List<Transaction> receipts;
}
