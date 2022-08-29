package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;

public interface JuridicalPersonService {
    JuridicalPerson save(JuridicalPersonDTO entity);
}
