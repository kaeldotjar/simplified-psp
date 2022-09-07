package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.ShopkeeperDTO;
import io.github.zam0k.simplifiedpsp.domain.Shopkeeper;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopkeeperUserServiceImplTest {

  public static final UUID ID = UUID.randomUUID();
  public static final String FULL_NAME = "subject name";
  public static final String CNPJ = "68.340.815/0001-53";
  public static final String EMAIL = "subject@gmail.com";
  public static final String PASSWORD = "123456";
  public static final BigDecimal BALANCE = BigDecimal.valueOf(100.00);

  @InjectMocks private ShopkeeperServiceImpl service;

  @Mock private ShopkeeperRepository repository;
  @Mock private ModelMapper mapper;
  @Mock private PagedResourcesAssembler<?> assembler;

  private Shopkeeper shopkeeper;
  private ShopkeeperDTO shopkeeperDTO;

  @BeforeEach
  void setUp() {
    shopkeeper = new Shopkeeper(ID, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE);
    shopkeeperDTO = new ShopkeeperDTO(ID, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE);
  }

  @Test
  void whenSaveThenReturnSuccess() {
    when(mapper.map(any(ShopkeeperDTO.class), any())).thenReturn(shopkeeper);
    when(mapper.map(any(Shopkeeper.class), any())).thenReturn(shopkeeperDTO);

    when(repository.save(any())).thenReturn(shopkeeper);

    ShopkeeperDTO response = service.save(shopkeeperDTO);

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(ShopkeeperDTO.class, response.getClass()),
        () -> assertEquals(ID, response.getKey()),
        () -> assertEquals(FULL_NAME, response.getFullName()),
        () -> assertEquals(CNPJ, response.getCnpj()),
        () -> assertEquals(EMAIL, response.getEmail()),
        () -> assertEquals(PASSWORD, response.getPassword()),
        () -> assertEquals(BALANCE, response.getBalance()));
  }

  @Test
  void whenFindByIdThenReturnShop() {
    when(mapper.map(any(Shopkeeper.class), any())).thenReturn(shopkeeperDTO);
    when(repository.findById(any())).thenReturn(Optional.of(shopkeeper));

    ShopkeeperDTO response = service.findById(ID);

    // TO-DO: better tests for Hateoas
    assertAll(
        () -> assertNotNull(response),
        () -> assertNotNull(response.getLinks()),
        () -> assertTrue(response.hasLink("self")),
        () -> assertTrue(response.hasLink("shopkeepers")),
        () -> assertEquals(ShopkeeperDTO.class, response.getClass()),
        () -> assertEquals(ID, response.getKey()),
        () -> assertEquals(FULL_NAME, response.getFullName()),
        () -> assertEquals(CNPJ, response.getCnpj()),
        () -> assertEquals(EMAIL, response.getEmail()),
        () -> assertEquals(PASSWORD, response.getPassword()),
        () -> assertEquals(BALANCE, response.getBalance()));
  }
}
