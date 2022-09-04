package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperUserRepository;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperUserService;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopkeeperUserServiceImpl implements ShopkeeperUserService {

    private final ModelMapper mapper;
    private final ShopkeeperUserRepository repository;

    @Override
    public ShopkeeperUserDTO save(ShopkeeperUserDTO entity) {

        Optional<ShopkeeperUser> cnpj = repository.findByCnpj(entity.getCnpj());

        if(cnpj.isPresent()) throw new BadRequestException("Cnpj already taken");

        Optional<ShopkeeperUser> email = repository.findByEmail(entity.getEmail());

        if(email.isPresent()) throw new BadRequestException("Email already taken");

        ShopkeeperUser shop = repository.save(mapper.map(entity, ShopkeeperUser.class));

        return mapper.map(shop, ShopkeeperUserDTO.class);
    }

    @Override
    public Page<ShopkeeperUserDTO> findAll(Pageable pageable) {

        return repository.findAll(pageable).map(e -> mapper.map(e, ShopkeeperUserDTO.class));
    }

    @Override
    public ShopkeeperUserDTO findOneById(Long id) {
        ShopkeeperUser entity = repository.findById(id).orElseThrow(NotFoundException::new);

        return mapper.map(entity, ShopkeeperUserDTO.class);
    }

}
