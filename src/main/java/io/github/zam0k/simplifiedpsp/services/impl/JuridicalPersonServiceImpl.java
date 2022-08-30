package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.services.JuridicalPersonService;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuridicalPersonServiceImpl implements JuridicalPersonService {

    private final JuridicalPersonRepository repository;
    private final NaturalPersonRepository naturalPersonRepository;
    private final ModelMapper mapper;

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
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }


    private List<NaturalPerson> getAllOwners(JuridicalPersonDTO entity) {
        List<Long> ids = entity.getOwners().stream().map(NaturalPersonDTO::getId).collect(Collectors.toList());
        List<NaturalPerson> owners = naturalPersonRepository.findAllById(ids);
        if(owners.size() < entity.getOwners().size()) throw new NotFoundException();
        return owners;
    }

}
