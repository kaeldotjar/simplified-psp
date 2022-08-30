package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;

import java.util.List;

public interface JuridicalPersonService {
    JuridicalPerson save(JuridicalPersonDTO entity);
    List<JuridicalPerson> findAll();
    JuridicalPerson findOneById(Long id);
}
