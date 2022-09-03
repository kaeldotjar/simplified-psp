package io.github.zam0k.simplifiedpsp.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "common_user")
@Setter @Getter @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
public final class CommonUser implements IPayer, IPayee {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;
    @Column(nullable = false, unique = true, length = 14, updatable = false)
    private String cpf;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private BigDecimal balance;

    @Override
    public void receiveValue(BigDecimal value) {
        setBalance(getBalance().add(value));
    }

    @Override
    public void removeValue(BigDecimal value) {
        setBalance(getBalance().subtract(value));
    }
}
