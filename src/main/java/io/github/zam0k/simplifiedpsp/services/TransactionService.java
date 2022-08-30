package io.github.zam0k.simplifiedpsp.services;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.Transaction;

public interface TransactionService {
    Transaction create(TransactionDTO transaction);
}
