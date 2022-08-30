package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.IPayee;
import io.github.zam0k.simplifiedpsp.domain.IPayer;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private JuridicalPersonRepository juridicalPersonRepository;
    
    @Autowired
    private ModelMapper mapper;

    @Override
    public Transaction create(TransactionDTO entity) {

        Transaction transaction = mapper.map(entity, Transaction.class);
        IPayer payer = getPayer(transaction);
        IPayee payee = getPayee(transaction);

        BigDecimal value = transaction.getValue();
        payee.receiveValue(value);
        payer.removeValue(value);

        return repository.save(transaction);
    }

    private IPayee getPayee(Transaction transaction) {
        Long payeeId = transaction.getPayee();
        // TO-DO: custom exception: EntityNotFoundException???
        return Stream.of(naturalPersonRepository.findById(payeeId), juridicalPersonRepository.findById(payeeId))
                .filter(Optional::isPresent).map(Optional::get).findFirst().orElseThrow(RuntimeException::new);

    }

    private IPayer getPayer(Transaction transaction) {
        Long payerId = transaction.getPayer();
        // TO-DO: custom exception: BusinessLogicException() BAD-REQUEST
        if(juridicalPersonRepository.findById(payerId).isPresent()) throw new RuntimeException();
        // TO-DO: custom exception: EntityNotFoundException???
        return naturalPersonRepository.findById(payerId).orElseThrow(RuntimeException::new);
    }
}
