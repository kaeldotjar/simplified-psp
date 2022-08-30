package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
