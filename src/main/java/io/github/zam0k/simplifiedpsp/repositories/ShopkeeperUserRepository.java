package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopkeeperUserRepository extends JpaRepository<ShopkeeperUser, Long> {
    Optional<ShopkeeperUser> findByCnpj(String cnpj);
    Optional<ShopkeeperUser> findByEmail(String email);
}
