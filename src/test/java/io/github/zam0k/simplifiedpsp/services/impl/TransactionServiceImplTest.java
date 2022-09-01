package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.utils.PaymentNotifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

class TransactionServiceImplTest {

    public final static Long PAYER_ID = 1L;
    public final static String PAYER_FULL_NAME = "subject name";
    public final static String PAYER_CPF = "275.974.380-28";
    public final static String PAYER_EMAIL = "subject@gmail.com";
    public final static String PAYER_PASSWORD = "123456";
    public final static BigDecimal PAYER_BALANCE = BigDecimal.valueOf(100.00);

    public final static Long PAYEE_ID = 2L;
    public final static String PAYEE_FULL_NAME = "subject shop";
    public final static String PAYEE_CNPJ = "68.340.815/0001-53";
    public final static String PAYEE_EMAIL = "subjectshop@gmail.com";
    public final static String PAYEE_PASSWORD = "123456";
    public final static BigDecimal PAYEE_BALANCE = BigDecimal.valueOf(100.00);

    public final static Long TRANS_ID = 3L;
    public final static BigDecimal TRANS_VALUE = BigDecimal.valueOf(50.00);

    @InjectMocks
    private TransactionServiceImpl service;

    @Mock
    private TransactionRepository repository;
    @Mock
    private NaturalPersonRepository naturalPersonRepository;
    @Mock
    private JuridicalPersonRepository juridicalPersonRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PaymentNotifier notifier;

    private Transaction transaction;
    private TransactionDTO transactionDTO;

    private NaturalPerson payer;
    private Optional<NaturalPerson> optionalPayer;

    private JuridicalPerson payee;
    private JuridicalPersonDTO payeeDTO;

    private AutoCloseable closeable;
    private MockRestServiceServer server;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        transactionDTO = new TransactionDTO(PAYER_ID, PAYEE_ID, TRANS_VALUE);
        transaction = new Transaction(TRANS_ID, PAYER_ID, PAYEE_ID, TRANS_VALUE, LocalDateTime.now());

        payer = new NaturalPerson(PAYER_ID, PAYER_FULL_NAME, PAYER_CPF,
                PAYER_EMAIL, PAYER_PASSWORD, PAYER_BALANCE, null);
        optionalPayer = Optional.ofNullable(payer);

        payee = new JuridicalPerson(PAYEE_ID, PAYEE_FULL_NAME, PAYEE_CNPJ,
                PAYEE_EMAIL, PAYEE_PASSWORD, PAYEE_BALANCE, new ArrayList<>());

        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void WhenCreatedTransactionThenReturnSuccess() {
        //TO-DO: make this test better

        String AuthURI = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";

        when(mapper.map(any(), any())).thenReturn(transaction);
        when(naturalPersonRepository.findById(any())).thenReturn(optionalPayer);
        ResponseEntity responseEntity = new ResponseEntity(OK);
        when(restTemplate.getForEntity(AuthURI, String.class))
                .thenReturn(responseEntity);

        when(repository.save(any())).thenReturn(transaction);

        Transaction response = service.create(transactionDTO);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(Transaction.class, response.getClass()),
                () -> assertEquals(TRANS_ID, response.getId()),
                () -> assertEquals(TRANS_VALUE, response.getValue())
        );
    }

    //TO-DO: test insufficient funds exception
    //TO-DO: test Transaction rejected exception
}