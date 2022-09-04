package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopkeeperUserService {

    ShopkeeperUserDTO save(ShopkeeperUserDTO entity);
    Page<ShopkeeperUserDTO> findAll(Pageable pageable);
    ShopkeeperUserDTO findOneById(Long id);

}
