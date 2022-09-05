package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface CommonUserService {
    CommonUserDTO save(CommonUserDTO entity);
    CommonUserDTO findById(Long id);
    PagedModel<EntityModel<TransactionDTO>> findTransactions(Long id, Pageable pageable);
}
