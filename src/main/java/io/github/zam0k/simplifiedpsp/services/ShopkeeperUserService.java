package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;

import java.util.List;

public interface ShopkeeperUserService {

    ShopkeeperUserDTO save(ShopkeeperUserDTO entity);
    List<ShopkeeperUserDTO> findAll();
    ShopkeeperUserDTO findOneById(Long id);

}
