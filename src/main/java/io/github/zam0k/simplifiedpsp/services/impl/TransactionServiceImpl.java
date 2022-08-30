package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.IPayee;
import io.github.zam0k.simplifiedpsp.domain.IPayer;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.TransactionService;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final NaturalPersonRepository naturalPersonRepository;
    private final JuridicalPersonRepository juridicalPersonRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public Transaction create(TransactionDTO entity) {

        Transaction transaction = mapper.map(entity, Transaction.class);
        BigDecimal value = transaction.getValue();
        IPayer payer = getPayer(transaction);

        if(value.compareTo(payer.getBalance()) >= 0)
            throw new BadRequestException("Insufficient funds");

        IPayee payee = getPayee(transaction);

        payee.receiveValue(value);
        payer.removeValue(value);

        return repository.save(transaction);
    }

    private IPayee getPayee(Transaction transaction) {
        Long payeeId = transaction.getPayee();
        return Stream.of(naturalPersonRepository.findById(payeeId), juridicalPersonRepository.findById(payeeId))
                .filter(Optional::isPresent).map(Optional::get).findFirst()
                .orElseThrow(NotFoundException::new);

    }

    private IPayer getPayer(Transaction transaction) {
        Long payerId = transaction.getPayer();
        return naturalPersonRepository.findById(payerId).orElseThrow(NotFoundException::new);
    }
}
