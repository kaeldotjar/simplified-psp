package io.github.zam0k.simplifiedpsp.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "payer_id", nullable = false)
    private UUID payer;

    @Column(name = "payee_id", nullable = false)
    private UUID payee;

    @Column(name = "`value`", nullable = false)
    private BigDecimal value;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    private void createdAt() {
        this.timestamp = LocalDateTime.now();
    }
}
