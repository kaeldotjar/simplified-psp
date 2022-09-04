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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        List<ShopkeeperUser> shopkeepers = List.of(entity);
        Pageable pageable = PageRequest.of(0, 10);

        when(mapper.map(any(ShopkeeperUser.class), any())).thenReturn(entityDTO);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(shopkeepers, pageable, shopkeepers.size()));

        Page<ShopkeeperUserDTO> response = service.findAll(pageable);
        ShopkeeperUserDTO firstEntityOnResponse = response.getContent().get(0);

        assertAll(
                () -> assertEquals(true, response.hasContent()),
                () -> assertEquals(1, response.getNumberOfElements()),
                () -> assertEquals(ShopkeeperUserDTO.class, firstEntityOnResponse.getClass()),
                () -> assertEquals(ID, firstEntityOnResponse.getId()),
                () -> assertEquals(FULL_NAME, firstEntityOnResponse.getFullName()),
                () -> assertEquals(CNPJ, firstEntityOnResponse.getCnpj()),
                () -> assertEquals(EMAIL, firstEntityOnResponse.getEmail()),
                () -> assertEquals(PASSWORD, firstEntityOnResponse.getPassword()),
                () -> assertEquals(BALANCE, firstEntityOnResponse.getBalance())
        );
    }

    @Test
    void whenFindByIdThenReturnShop() {
        when(mapper.map(any(ShopkeeperUser.class), any())).thenReturn(entityDTO);
        when(repository.findById(any())).thenReturn(optionalEntity);

        ShopkeeperUserDTO response = service.findOneById(ID);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ShopkeeperUserDTO.class, response.getClass()),
                () -> assertEquals(ID, response.getId()),
                () -> assertEquals(FULL_NAME, response.getFullName()),
                () -> assertEquals(CNPJ, response.getCnpj()),
                () -> assertEquals(EMAIL, response.getEmail()),
                () -> assertEquals(PASSWORD, response.getPassword()),
                () -> assertEquals(BALANCE, response.getBalance())
        );
    }

}