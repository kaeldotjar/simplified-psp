package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.CommonUserRepository;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperUserRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import io.github.zam0k.simplifiedpsp.utils.PaymentNotifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
class TransactionServiceImplTest {
    public final static Long TRANS_ID = 3L;
    public final static BigDecimal TRANS_BALANCE = BigDecimal.valueOf(10.00);
    public final static LocalDateTime TRANS_TIMESTAMP = LocalDateTime.now();

    public final static Long PAYER_ID = 2L;
    public final static BigDecimal PAYER_BALANCE = BigDecimal.valueOf(100.00);

    public final static Long PAYEE_ID = 2L;
    public final static BigDecimal PAYEE_BALANCE = BigDecimal.valueOf(100.00);


    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private CommonUserRepository payerRepository;
    @Mock
    private ShopkeeperUserRepository payeeRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PaymentNotifier notifier;
    @Mock
    private TransactionRepository repository;

    private TransactionDTO transactionDto;
    private Transaction transaction;
    private Optional<Transaction> optionalTransaction;
    private CommonUser payer;
    private ShopkeeperUser payee;
    private Optional<CommonUser> optionalPayer;



    @BeforeEach
    void setUp() {
        transactionDto = new TransactionDTO(TRANS_ID, PAYER_ID, PAYEE_ID, TRANS_BALANCE, TRANS_TIMESTAMP);
        transaction = new Transaction(TRANS_ID, PAYER_ID, PAYEE_ID, TRANS_BALANCE, TRANS_TIMESTAMP);
        optionalTransaction = Optional.of(transaction);
        payer = new CommonUser(PAYER_ID, "", "", "",
                "", PAYER_BALANCE);
        payee = new ShopkeeperUser(PAYEE_ID, "", "",
                "", "", PAYEE_BALANCE);
        optionalPayer = Optional.of(payer);
    }


    @Test
    void whenSaveTransactionReturnSuccess() {
        when(payerRepository.findById(any())).thenReturn(optionalPayer);
        when(payeeRepository.findById(any())).thenReturn(Optional.of(payee));
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(OK));
        doNothing().when(notifier).notifyPayee(any(), any());
        when(repository.save(any())).thenReturn(transaction);

        when(mapper.map(any(Transaction.class), any())).thenReturn(transactionDto);

        TransactionDTO response = service.create(transactionDto);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(TransactionDTO.class, response.getClass()),
                () -> assertEquals(TRANS_ID, response.getKey()),
                () -> assertEquals(TRANS_BALANCE, response.getValue()),
                () -> assertEquals(PAYEE_ID, response.getPayee()),
                () -> assertEquals(PAYER_ID, response.getPayer()),
                () -> assertEquals(TRANS_TIMESTAMP, response.getTimestamp())
        );
    }

    @Test
    void whenSaveTransactionReturnBadRequestInsufficientFunds() {
        transactionDto.setValue(BigDecimal.valueOf(500.00));

        when(payerRepository.findById(any())).thenReturn(Optional.of(payer));
        when(payeeRepository.findById(any())).thenReturn(Optional.of(payee));

        try {
            service.create(transactionDto);
        } catch (Exception ex) {
            assertAll(
                    () -> assertEquals(BadRequestException.class, ex.getClass()),
                    () -> assertEquals("Insufficient funds", ex.getMessage())
            );
        }
    }

    @Test
    void whenSaveTransactionReturnBadRequestTransactionRejected() {

        when(payerRepository.findById(any())).thenReturn(Optional.of(payer));
        when(payeeRepository.findById(any())).thenReturn(Optional.of(payee));
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity<>(BAD_REQUEST));

        try {
            service.create(transactionDto);
        } catch (Exception ex) {
            assertAll(
                    () -> assertEquals(BadRequestException.class, ex.getClass()),
                    () -> assertEquals("Transaction rejected", ex.getMessage())
            );
        }
    }

    @Test
    void whenSaveTransactionReturnNotFoundException() {
        transactionDto.setPayer(4L);
        try {
            service.create(transactionDto);
        } catch (Exception ex) {
            assertAll(
                    () -> assertEquals(NotFoundException.class, ex.getClass()),
                    () -> assertEquals("Object cannot be found", ex.getMessage())
            );
        }
    }

    @Test
    void whenFindByIdReturnSuccess() {
        when(repository.findById(TRANS_ID)).thenReturn(optionalTransaction);
        when(mapper.map(any(Transaction.class), any())).thenReturn(transactionDto);

        TransactionDTO response = service.findById(TRANS_ID);

        assertAll(
                () -> assertNotNull(response),
                () -> assertTrue(response.hasLinks()),
                () -> assertTrue(response.hasLink("self")),
                () -> assertFalse(response.getLinks("self").isEmpty()),
                () -> assertEquals(TransactionDTO.class, response.getClass()),
                () -> assertEquals(TRANS_ID, response.getKey()),
                () -> assertEquals(TRANS_BALANCE, response.getValue()),
                () -> assertEquals(PAYEE_ID, response.getPayee()),
                () -> assertEquals(PAYER_ID, response.getPayer()),
                () -> assertEquals(TRANS_TIMESTAMP, response.getTimestamp())
        );
    }

    @Test
    void whenFindByIdReturnNotFoundException() {

        try {
            service.findById(TRANS_ID);
        }catch (Exception ex) {
            assertAll(
                    () -> assertEquals(NotFoundException.class, ex.getClass()),
                    () -> assertEquals("Object cannot be found", ex.getMessage())
            );
        }
    }
}