package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.services.JuridicalPersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JuridicalPersonServiceImpl implements JuridicalPersonService {

    @Autowired
    private JuridicalPersonRepository repository;
    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public JuridicalPerson save(JuridicalPersonDTO entity) {

        List<NaturalPerson> owners = getAllOwners(entity);
        entity.getOwners().clear();
        JuridicalPerson person = mapper.map(entity, JuridicalPerson.class);
        person.addOwners(owners);

        return repository.save(person);
    }

    @Override
    public List<JuridicalPerson> findAll() {
        return repository.findAll();
    }

    @Override
    public JuridicalPerson findOneById(Long id) {
        // TO-DO: create a custom error for this (MissingEntityException?)
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }


    private List<NaturalPerson> getAllOwners(JuridicalPersonDTO entity) {
        List<Long> ids = entity.getOwners().stream().map(NaturalPersonDTO::getId).collect(Collectors.toList());
        List<NaturalPerson> owners = naturalPersonRepository.findAllById(ids);
        // TO-DO: create a custom error for this (MissingEntityException?)
        if(owners.size() < entity.getOwners().size()) throw new RuntimeException();
        return owners;
    }

}
