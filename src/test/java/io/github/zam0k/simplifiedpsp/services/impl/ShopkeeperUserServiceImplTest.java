package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopkeeperUserServiceImplTest {

    public final static Long ID = 2L;
    public final static String FULL_NAME = "subject name";
    public final static String CNPJ = "68.340.815/0001-53";
    public final static String EMAIL = "subject@gmail.com";
    public final static String PASSWORD = "123456";
    public final static BigDecimal BALANCE = BigDecimal.valueOf(100.00);

    @InjectMocks
    private ShopkeeperUserServiceImpl service;

    @Mock
    private ShopkeeperUserRepository repository;
    @Mock
    private ModelMapper mapper;

    private ShopkeeperUser entity;
    private ShopkeeperUserDTO entityDTO;
    private Optional<ShopkeeperUser> optionalEntity;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        entity = new ShopkeeperUser(ID, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE);
        entityDTO = new ShopkeeperUserDTO(FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE);
        optionalEntity = Optional.of(entity);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void whenSaveThenReturnSuccess() {
        when(repository.save(any())).thenReturn(entity);

        ShopkeeperUser response = service.save(entityDTO);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ShopkeeperUser.class, response.getClass()),
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

        List<ShopkeeperUser> response = service.findAll();

        assertAll(
                () -> assertEquals(1, response.size()),
                () -> assertEquals(ShopkeeperUser.class, response.get(0).getClass()),
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

        ShopkeeperUser response = service.findOneById(ID);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ShopkeeperUser.class, response.getClass()),
                () -> assertEquals(ID, response.getId()),
                () -> assertEquals(FULL_NAME, response.getFullName()),
                () -> assertEquals(CNPJ, response.getCnpj()),
                () -> assertEquals(EMAIL, response.getEmail()),
                () -> assertEquals(PASSWORD, response.getPassword()),
                () -> assertEquals(BALANCE, response.getBalance())
        );
    }

}