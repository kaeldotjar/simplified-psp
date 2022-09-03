package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;

import java.util.List;

public interface ShopkeeperUserService {

    ShopkeeperUser save(ShopkeeperUserDTO entity);
    List<ShopkeeperUser> findAll();
    ShopkeeperUser findOneById(Long id);

}
