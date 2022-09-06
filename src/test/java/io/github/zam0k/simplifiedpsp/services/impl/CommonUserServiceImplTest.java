package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.repositories.CommonUserRepository;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommonUserServiceImplTest {

  public static final UUID ID = UUID.randomUUID();
  public static final String FULL_NAME = "subject name";
  public static final String CPF = "275.974.380-28";
  public static final String EMAIL = "subject@gmail.com";
  public static final String PASSWORD = "123456";
  public static final BigDecimal BALANCE = BigDecimal.valueOf(100.00);

  @InjectMocks private CommonUserServiceImpl service;

  @Mock private CommonUserRepository repository;
  @Mock private ModelMapper mapper;

  private CommonUser entity;
  private CommonUserDTO entityDTO;
  private Optional<CommonUser> optionalEntity;

  @BeforeEach
  void setUp() {
    entity = new CommonUser(ID, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
    entityDTO = new CommonUserDTO(ID, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
    optionalEntity = Optional.of(entity);
  }

  @Test
  void whenSaveThenReturnSuccess() {
    when(mapper.map(any(CommonUserDTO.class), any())).thenReturn(entity);
    when(mapper.map(any(CommonUser.class), any())).thenReturn(entityDTO);
    Mockito.when(repository.save(any())).thenReturn(entity);

    CommonUserDTO response = service.save(entityDTO);

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(CommonUserDTO.class, response.getClass()),
        () -> assertEquals(ID, response.getKey()),
        () -> assertEquals(FULL_NAME, response.getFullName()),
        () -> assertEquals(CPF, response.getCpf()),
        () -> assertEquals(EMAIL, response.getEmail()),
        () -> assertEquals(PASSWORD, response.getPassword()),
        () -> assertEquals(BALANCE, response.getBalance()));
  }

  @Test
  void whenSaveRepeatedCPFThenReturnBadRequestException() {
    when(repository.findByCpf(anyString())).thenReturn(optionalEntity);

    try {
      service.save(entityDTO);
    } catch (Exception ex) {
      assertAll(
          () -> assertEquals(BadRequestException.class, ex.getClass()),
          () -> assertEquals("Cpf must be unique", ex.getMessage()));
    }
  }

  @Test
  void whenSaveRepeatedEmailThenReturnBadRequestException() {
    when(repository.findByEmail(anyString())).thenReturn(optionalEntity);

    try {
      service.save(entityDTO);
    } catch (Exception ex) {
      assertAll(
          () -> assertEquals(BadRequestException.class, ex.getClass()),
          () -> assertEquals("Email must be unique", ex.getMessage()));
    }
  }
}
