package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JuridicalPersonServiceImplTest {

    public final static Long ID = 2L;
    public final static String FULL_NAME = "subject name";
    public final static String CNPJ = "68.340.815/0001-53";
    public final static String EMAIL = "subject@gmail.com";
    public final static String PASSWORD = "123456";
    public final static BigDecimal BALANCE = BigDecimal.valueOf(100.00);

    public final static Long OWNER_ID = 1L;
    public final static String OWNER_FULL_NAME = "jane doe";
    public final static String OWNER_CPF = "123456";
    public final static String OWNER_EMAIL = "owner@email.com";
    public final static String OWNER_PASSWORD = "123456456";
    public final static BigDecimal OWNER_BALANCE = BigDecimal.valueOf(100.00);

    @InjectMocks
    private JuridicalPersonServiceImpl service;

    @Mock
    private JuridicalPersonRepository repository;
    @Mock
    private NaturalPersonRepository ownerRepository;
    @Mock
    private ModelMapper mapper;

    private NaturalPersonDTO ownerDTO;
    private NaturalPerson owner;
    private JuridicalPerson entity;
    private JuridicalPersonDTO entityDTO;
    private Optional<JuridicalPerson> optionalEntity;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        List<NaturalPerson> allOwners = new ArrayList<>();
        List<NaturalPersonDTO> allOwnersDTO = new ArrayList<>();
        List<JuridicalPerson> allShops = new ArrayList<>();

        closeable = MockitoAnnotations.openMocks(this);
        ownerDTO = new NaturalPersonDTO(OWNER_ID, OWNER_CPF, OWNER_FULL_NAME, OWNER_EMAIL,
                OWNER_PASSWORD, OWNER_BALANCE);
        owner = new NaturalPerson(OWNER_ID, OWNER_CPF, OWNER_FULL_NAME, OWNER_EMAIL,
                OWNER_PASSWORD, OWNER_BALANCE, null);

        allOwners.add(owner);
        entity = new JuridicalPerson(ID, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE, allOwners);
        entityDTO = new JuridicalPersonDTO(FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE, allOwnersDTO);
        optionalEntity = Optional.of(new JuridicalPerson(ID, FULL_NAME, CNPJ, EMAIL,
                PASSWORD, BALANCE, allOwners));

        owner.setShops(allShops);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void whenSaveThenReturnSuccess() {

        when(ownerRepository.findAllById(any())).thenReturn(List.of(owner));
        when(mapper.map(any(), any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(any())).thenReturn(optionalEntity);


        JuridicalPerson response = service.save(entityDTO);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(JuridicalPerson.class, response.getClass()),
                () -> assertEquals(ID, response.getId()),
                () -> assertEquals(FULL_NAME, response.getFullName()),
                () -> assertEquals(CNPJ, response.getCnpj()),
                () -> assertEquals(EMAIL, response.getEmail()),
                () -> assertEquals(PASSWORD, response.getPassword()),
                () -> assertEquals(BALANCE, response.getBalance())
        );
    }

    @Test
    void findAll() {

    }

    @Test
    void findOneById() {
    }

    @Test
    void addNewOwner() {
    }
}