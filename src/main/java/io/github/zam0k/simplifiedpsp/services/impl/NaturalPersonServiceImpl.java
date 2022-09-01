package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.services.NaturalPersonService;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NaturalPersonServiceImpl implements NaturalPersonService {

    @Autowired
    private NaturalPersonRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public NaturalPerson save(NaturalPersonDTO entity) {
        Optional<NaturalPerson> cpfAlreadyExists = repository.findByCpf(entity.getCpf());

        if(cpfAlreadyExists.isPresent())
            throw new BadRequestException("Cpf must be unique");

        Optional<NaturalPerson> emailAlreadyExists = repository.findByEmail(entity.getEmail());

        if (emailAlreadyExists.isPresent())
            throw new BadRequestException("Email must be unique");

        return repository.save(mapper.map(entity, NaturalPerson.class));
    }
}
