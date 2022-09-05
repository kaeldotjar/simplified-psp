package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommonUserRepository extends JpaRepository<CommonUser, UUID> {
  Optional<CommonUser> findByCpf(String cpf);

  Optional<CommonUser> findByEmail(String email);
}
