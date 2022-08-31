package io.github.zam0k.simplifiedpsp.services.impl;

import io.github.zam0k.simplifiedpsp.controllers.dto.NaturalPersonDTO;
import io.github.zam0k.simplifiedpsp.domain.NaturalPerson;
import io.github.zam0k.simplifiedpsp.repositories.NaturalPersonRepository;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class NaturalPersonServiceImplTest {

    public final static Long ID = 1L;
    public final static String FULL_NAME = "subject name";
    public final static String CPF = "275.974.380-28";
    public final static String EMAIL = "subject@gmail.com";
    public final static String PASSWORD = "123456";
    public final static BigDecimal BALANCE = BigDecimal.valueOf(100.00);

    @InjectMocks
    private NaturalPersonServiceImpl service;

    @Mock
    private NaturalPersonRepository repository;
    @Mock
    private ModelMapper mapper;

    private NaturalPerson entity;
    private NaturalPersonDTO entityDTO;
    private Optional<NaturalPerson> optionalEntity;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        entity = new NaturalPerson(ID, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE, null);
        entityDTO = new NaturalPersonDTO(ID, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
        optionalEntity = Optional.of(new NaturalPerson(ID, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE, null));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void whenSaveThenReturnSuccess() {
        when(repository.save(any())).thenReturn(entity);

        NaturalPerson response = service.save(entityDTO);


        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(NaturalPerson.class, response.getClass()),
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