package io.github.zam0k.simplifiedpsp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "payer_id", nullable = false, columnDefinition = "BINARY(16)")
  private UUID payer;

  @Column(name = "payee_id", nullable = false, columnDefinition = "BINARY(16)")
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
