package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t FROM Transaction t WHERE t.payee = :id OR t.payer =:id")
    Page<Transaction> findAllOwnerTransactions(@Param("id") UUID id, Pageable pageable);
    Page<Transaction> findAllByPayee(UUID id, Pageable pageable);
}
