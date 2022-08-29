package io.github.zam0k.simplifiedpsp.repositories;

import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaturalPersonRepository extends JpaRepository<NaturalPerson, Long> {
}
