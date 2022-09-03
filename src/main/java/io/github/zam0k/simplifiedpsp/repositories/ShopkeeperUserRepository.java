package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopkeeperUserRepository extends JpaRepository<ShopkeeperUser, Long> {
}
