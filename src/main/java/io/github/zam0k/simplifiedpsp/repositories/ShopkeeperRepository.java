package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.Shopkeeper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShopkeeperRepository extends JpaRepository<Shopkeeper, UUID> {
  Optional<Shopkeeper> findByCnpj(String cnpj);

  Optional<Shopkeeper> findByEmail(String email);
}
