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

@Service
@RequiredArgsConstructor
public class ShopkeeperUserServiceImpl implements ShopkeeperUserService {

    private final ModelMapper mapper;
    private final ShopkeeperUserRepository repository;

    @Override
    public ShopkeeperUser save(ShopkeeperUserDTO entity) {

        ShopkeeperUser shop = mapper.map(entity, ShopkeeperUser.class);

        return repository.save(shop);
    }

    @Override
    public List<ShopkeeperUser> findAll() {
        return repository.findAll();
    }

    @Override
    public ShopkeeperUser findOneById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

}
