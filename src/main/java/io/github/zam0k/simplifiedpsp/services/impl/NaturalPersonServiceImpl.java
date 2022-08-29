package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.services.NaturalPersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NaturalPersonServiceImpl implements NaturalPersonService {

    @Autowired
    private NaturalPersonRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public NaturalPerson save(NaturalPersonDTO entity) {
        return repository.save(mapper.map(entity, NaturalPerson.class));
    }
}
