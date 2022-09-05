package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperUserDTO;
import io.github.zam0k.simplifiedpsp.domain.ShopkeeperUser;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PagedResourcesAssembler;

import java.math.BigDecimal;
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
    @Mock
    private PagedResourcesAssembler assembler;

    private ShopkeeperUser entity;
    private ShopkeeperUserDTO entityDTO;
    private Optional<ShopkeeperUser> optionalEntity;

    @BeforeEach
    void setUp() {
        entity = new ShopkeeperUser(ID, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE);
        entityDTO = new ShopkeeperUserDTO(ID, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE);
        optionalEntity = Optional.of(entity);
    }

    @Test
    void whenSaveThenReturnSuccess() {
        when(mapper.map(any(ShopkeeperUserDTO.class), any())).thenReturn(entity);
        when(mapper.map(any(ShopkeeperUser.class), any())).thenReturn(entityDTO);

        when(repository.save(any())).thenReturn(entity);

        ShopkeeperUserDTO response = service.save(entityDTO);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ShopkeeperUserDTO.class, response.getClass()),
                () -> assertEquals(ID, response.getKey()),
                () -> assertEquals(FULL_NAME, response.getFullName()),
                () -> assertEquals(CNPJ, response.getCnpj()),
                () -> assertEquals(EMAIL, response.getEmail()),
                () -> assertEquals(PASSWORD, response.getPassword()),
                () -> assertEquals(BALANCE, response.getBalance())
        );
    }

    /* TO-DO: make this test in the ControllerJsonTest since (it seems) you can't unit test a PagedModel
    @Test
    void whenFindAllThenReturnShopList() {
    }
    */

    @Test
    void whenFindByIdThenReturnShop() {
        when(mapper.map(any(ShopkeeperUser.class), any())).thenReturn(entityDTO);
        when(repository.findById(any())).thenReturn(optionalEntity);

        ShopkeeperUserDTO response = service.findById(ID);

        // TO-DO: better tests for Hateoas
        assertAll(
                () -> assertTrue(response.hasLinks()),
                () -> assertFalse(response.getLinks("shopkeepers").isEmpty()),
                () -> assertFalse(response.getLinks("self").isEmpty()),
                () -> assertNotNull(response),
                () -> assertEquals(ShopkeeperUserDTO.class, response.getClass()),
                () -> assertEquals(ID, response.getKey()),
                () -> assertEquals(FULL_NAME, response.getFullName()),
                () -> assertEquals(CNPJ, response.getCnpj()),
                () -> assertEquals(EMAIL, response.getEmail()),
                () -> assertEquals(PASSWORD, response.getPassword()),
                () -> assertEquals(BALANCE, response.getBalance())
        );
    }

}