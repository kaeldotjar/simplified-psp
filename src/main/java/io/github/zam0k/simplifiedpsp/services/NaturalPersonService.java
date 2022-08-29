package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;

public interface NaturalPersonService {
    NaturalPerson save(NaturalPersonDTO entity);
}
