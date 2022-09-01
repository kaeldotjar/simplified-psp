package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.services.JuridicalPersonService;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
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
        JuridicalPerson shop = mapper.map(entity, JuridicalPerson.class);
        shop.addOwners(owners);

        return repository.save(shop);
    }

    @Override
    public List<JuridicalPerson> findAll() {
        return repository.findAll();
    }

    @Override
    public JuridicalPerson findOneById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public void addNewOwner(Long id, Long ownerId) {
        JuridicalPerson shop = this.findOneById(id);
        NaturalPerson owner = naturalPersonRepository.findById(ownerId).orElseThrow(NotFoundException::new);
        shop.getOwners().forEach(shopOwner -> {
            if (owner.equals(shopOwner)) {
                // TO-DO check if this is the correct error for this
                throw new BadRequestException("Owner already exists");
            }
        });
        shop.addOwner(owner);
        repository.save(shop);
    }

    private List<NaturalPerson> getAllOwners(JuridicalPersonDTO entity) {
        List<Long> ids = entity.getOwners().stream().map(NaturalPersonDTO::getId).collect(Collectors.toList());
        List<NaturalPerson> owners = naturalPersonRepository.findAllById(ids);
        if(owners.size() < entity.getOwners().size()) throw new NotFoundException();
        return owners;
    }

}
