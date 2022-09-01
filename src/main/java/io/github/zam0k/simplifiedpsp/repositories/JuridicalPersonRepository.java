package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuridicalPersonRepository extends JpaRepository<JuridicalPerson, Long> {
}
