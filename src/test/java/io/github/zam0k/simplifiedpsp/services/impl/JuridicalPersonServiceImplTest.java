package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.JuridicalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.JuridicalPerson;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.JuridicalPersonRepository;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
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

    private NaturalPerson owner;
    private JuridicalPerson entity;
    private JuridicalPersonDTO entityDTO;
    private Optional<JuridicalPerson> optionalEntity;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        owner = new NaturalPerson(OWNER_ID, OWNER_CPF, OWNER_FULL_NAME, OWNER_EMAIL,
                OWNER_PASSWORD, OWNER_BALANCE, new ArrayList<>());

        entity = new JuridicalPerson(ID, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE, new ArrayList<>());
        entityDTO = new JuridicalPersonDTO(FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE, new ArrayList<>());
        optionalEntity = Optional.of(entity);
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
    void whenFindAllThenReturnShopList() {
        when(repository.findAll()).thenReturn(List.of(entity));

        List<JuridicalPerson> response = service.findAll();

        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals(JuridicalPerson.class, response.get(0).getClass()),
                () -> assertEquals(ID, response.get(0).getId()),
                () -> assertEquals(FULL_NAME, response.get(0).getFullName()),
                () -> assertEquals(CNPJ, response.get(0).getCnpj()),
                () -> assertEquals(EMAIL, response.get(0).getEmail()),
                () -> assertEquals(PASSWORD, response.get(0).getPassword()),
                () -> assertEquals(BALANCE, response.get(0).getBalance())
        );

    }

    @Test
    void whenFindByIdThenReturnShop() {
        when(repository.findById(any())).thenReturn(optionalEntity);

        JuridicalPerson response = service.findOneById(ID);

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
    void whenAddNewOwnerThenReturnSuccess() {
        NaturalPerson newOwner = new NaturalPerson(3L, OWNER_FULL_NAME, OWNER_CPF, OWNER_EMAIL, OWNER_PASSWORD,
                OWNER_BALANCE, new ArrayList<>());
        when(repository.findById(any())).thenReturn(optionalEntity);
        when(ownerRepository.findById(3L)).thenReturn(Optional.ofNullable(newOwner));
        when(repository.save(any())).thenReturn(entity);

        service.addNewOwner(ID, 3L);

        assertAll(
                () -> assertEquals(1, entity.getOwners().size()),
                () -> assertEquals(NaturalPerson.class, entity.getOwners().get(0).getClass()),
                () -> assertEquals(3L, entity.getOwners().get(0).getId()),
                () -> assertEquals(OWNER_FULL_NAME, entity.getOwners().get(0).getFullName()),
                () -> assertEquals(OWNER_CPF, entity.getOwners().get(0).getCpf()),
                () -> assertEquals(OWNER_EMAIL, entity.getOwners().get(0).getEmail()),
                () -> assertEquals(OWNER_PASSWORD, entity.getOwners().get(0).getPassword()),
                () -> assertEquals(OWNER_BALANCE, entity.getOwners().get(0).getBalance())
        );
    }

    @Test
    void whenAddNewOwnerThenReturnBadRequest() {
        when(repository.findById(any())).thenReturn(optionalEntity);
        when(ownerRepository.findById(OWNER_ID)).thenReturn(Optional.ofNullable(owner));
        when(repository.save(any())).thenReturn(entity);
        entity.getOwners().add(owner);
        owner.getShops().add(entity);

        try {
            service.addNewOwner(ID, OWNER_ID);
        } catch (Exception ex) {
            assertAll(
                    () -> assertEquals(BadRequestException.class, ex.getClass()),
                    () -> assertEquals("Owner already exists", ex.getMessage())
            );
        }
    }
}