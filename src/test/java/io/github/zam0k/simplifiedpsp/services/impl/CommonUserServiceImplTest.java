package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.CommonUserDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.repositories.CommonUserRepository;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;

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

  public static final String NOT_FOUND = "Object cannot be found";
  public static final String EMAIL_BAD_REQUEST = "Email must be unique";
  public static final String CPF_BAD_REQUEST = "Cpf must be unique";

  private CommonUser commonUser;
  private CommonUserDTO commonUserDTO;

  @InjectMocks private CommonUserServiceImpl service;
  @Mock private CommonUserRepository repository;
  @Mock private ModelMapper mapper;

  @BeforeEach
  void setUp() {
    initCommonUser();
  }

  @Test
  void whenSaveThenReturnSuccess() {
    when(mapper.map(any(CommonUserDTO.class), any())).thenReturn(commonUser);
    when(mapper.map(any(CommonUser.class), any())).thenReturn(commonUserDTO);
    Mockito.when(repository.save(any())).thenReturn(commonUser);

    CommonUserDTO response = service.save(commonUserDTO);

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
    when(repository.findByCpf(anyString())).thenReturn(Optional.of(commonUser));

    try {
      service.save(commonUserDTO);
    } catch (Exception ex) {
      assertAll(
          () -> assertEquals(BadRequestException.class, ex.getClass()),
          () -> assertEquals(CPF_BAD_REQUEST, ex.getMessage()));
    }
  }

  @Test
  void whenSaveRepeatedEmailThenReturnBadRequestException() {
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(commonUser));

    try {
      service.save(commonUserDTO);
    } catch (Exception ex) {
      assertAll(
          () -> assertEquals(BadRequestException.class, ex.getClass()),
          () -> assertEquals(EMAIL_BAD_REQUEST, ex.getMessage()));
    }
  }

  @Test
  void whenFindByIdThenReturnSuccess() {
    when(mapper.map(any(CommonUser.class), any())).thenReturn(commonUserDTO);
    Mockito.when(repository.findById(any())).thenReturn(Optional.of(commonUser));

    CommonUserDTO response = service.findById(commonUserDTO.getKey());

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
  void whenFindByIdThenReturnNotFoundException() {
    try {
      service.findById(commonUserDTO.getKey());
    } catch (Exception ex) {
      assertAll(
          () -> assertEquals(NotFoundException.class, ex.getClass()),
          () -> assertEquals(NOT_FOUND, ex.getMessage()));
    }
  }

  @Test
  void whenFindTransactionsReturnNotFoundException() {
    try {
      service.findTransactions(UUID.randomUUID(), PageRequest.of(0, 10));
    } catch (Exception ex) {
      assertAll(
          () -> assertEquals(NotFoundException.class, ex.getClass()),
          () -> assertEquals(NOT_FOUND, ex.getMessage()));
    }
  }

  private void initCommonUser() {
    commonUser = new CommonUser(ID, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
    commonUserDTO = new CommonUserDTO(ID, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
  }
}
