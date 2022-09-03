package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperUserRepository;
import io.github.zam0k.simplifiedpsp.services.ShopkeeperUserService;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopkeeperUserServiceImpl implements ShopkeeperUserService {

    private final ModelMapper mapper;
    private final ShopkeeperUserRepository repository;

    @Override
    public ShopkeeperUserDTO save(ShopkeeperUserDTO entity) {

        ShopkeeperUser shop = repository.save(mapper.map(entity, ShopkeeperUser.class));
        return mapper.map(shop, ShopkeeperUserDTO.class);
    }

    @Override
    public List<ShopkeeperUserDTO> findAll() {

        return repository.findAll().stream().map(
                entity -> mapper.map(entity, ShopkeeperUserDTO.class)
        ).collect(Collectors.toList());
    }

    @Override
    public ShopkeeperUserDTO findOneById(Long id) {
        ShopkeeperUser entity = repository.findById(id).orElseThrow(NotFoundException::new);

        return mapper.map(entity, ShopkeeperUserDTO.class);
    }

}
