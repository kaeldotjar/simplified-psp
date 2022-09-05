package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface ShopkeeperUserService {

    ShopkeeperUserDTO save(ShopkeeperUserDTO entity);
    PagedModel<EntityModel<ShopkeeperUserDTO>> findAll(Pageable pageable);
    ShopkeeperUserDTO findById(Long id);
    PagedModel<EntityModel<TransactionDTO>> findTransactions(Long id, Pageable pageable);
}
