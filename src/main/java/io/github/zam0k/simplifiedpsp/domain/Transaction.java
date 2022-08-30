package io.github.zam0k.simplifiedpsp.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "payer_id")
    private Long payer;

    @Column(name = "payee_id")
    private Long payee;

    @Column(name = "`value`")
    private BigDecimal value;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    private void createdAt() {
        this.timestamp = LocalDateTime.now();
    }
}
