package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.UUID;

public interface ShopkeeperService {

    ShopkeeperDTO save(ShopkeeperDTO entity);
    PagedModel<EntityModel<ShopkeeperDTO>> findAll(Pageable pageable);
    ShopkeeperDTO findById(UUID id);
    PagedModel<EntityModel<TransactionDTO>> findTransactions(UUID id, Pageable pageable);
}
