package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;

import java.util.UUID;

public interface TransactionService {
    TransactionDTO create(TransactionDTO transaction);
    TransactionDTO findById(UUID id);
}
