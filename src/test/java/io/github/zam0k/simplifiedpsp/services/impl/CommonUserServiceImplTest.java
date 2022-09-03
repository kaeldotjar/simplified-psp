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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommonUserServiceImplTest {

    public final static Long ID = 1L;
    public final static String FULL_NAME = "subject name";
    public final static String CPF = "275.974.380-28";
    public final static String EMAIL = "subject@gmail.com";
    public final static String PASSWORD = "123456";
    public final static BigDecimal BALANCE = BigDecimal.valueOf(100.00);

    @InjectMocks
    private CommonUserServiceImpl service;

    @Mock
    private CommonUserRepository repository;
    @Mock
    private ModelMapper mapper;

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
        Mockito.when(repository.save(any())).thenReturn(entity);

        CommonUser response = service.save(entityDTO);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(CommonUser.class, response.getClass()),
                () -> assertEquals(ID, response.getId()),
                () -> assertEquals(FULL_NAME, response.getFullName()),
                () -> assertEquals(CPF, response.getCpf()),
                () -> assertEquals(EMAIL, response.getEmail()),
                () -> assertEquals(PASSWORD, response.getPassword()),
                () -> assertEquals(BALANCE, response.getBalance())
        );
    }

    @Test
    void whenSaveRepeatedCPFThenReturnBadRequestException() {
        when(repository.findByCpf(anyString())).thenReturn(optionalEntity);

        try {
            service.save(entityDTO);
        } catch (Exception ex) {
            assertAll(
                    () -> assertEquals(BadRequestException.class, ex.getClass()),
                    () -> assertEquals("Cpf must be unique", ex.getMessage())
            );
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
                    () -> assertEquals("Email must be unique", ex.getMessage())
            );
        }
    }
}