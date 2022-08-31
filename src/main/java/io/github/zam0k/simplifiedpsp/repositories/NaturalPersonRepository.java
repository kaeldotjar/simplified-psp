package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
    Optional<NaturalPerson> findByCpf(String cpf);
    Optional<NaturalPerson> findByEmail(String email);
}
