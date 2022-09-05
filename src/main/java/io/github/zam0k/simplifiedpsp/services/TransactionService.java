package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO create(TransactionDTO transaction);
    TransactionDTO findById(Long id);
}
