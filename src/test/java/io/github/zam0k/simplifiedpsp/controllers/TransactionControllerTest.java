package io.github.zam0k.simplifiedpsp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zam0k.simplifiedpsp.configs.TestConfig;
import io.github.zam0k.simplifiedpsp.containers.ContainerInit;
import io.github.zam0k.simplifiedpsp.containers.MySQL;
import io.github.zam0k.simplifiedpsp.controllers.dto.TransactionDTO;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.domain.Shopkeeper;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.CommonUserRepository;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(initializers = ContainerInit.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
class TransactionControllerTest {
  public static final BigDecimal TRANS_VALUE = getFormattedBalance(10.00);
  public static final UUID PAYER_ID = randomUUID();
  public static final BigDecimal PAYER_BALANCE = getFormattedBalance(100.00);
  public static final UUID PAYEE_ID = randomUUID();
  public static final BigDecimal PAYEE_BALANCE = getFormattedBalance(100.00);

  private static final String BASE_PATH = "/api/transactions/v1/";

  private static Transaction requestBody;
  private static CommonUser payer;
  private static Shopkeeper payee;
  private Response transaction;

  @Container private static final MySQL MYSQL_CONTAINER = MySQL.getInstance();
  @Autowired private TransactionRepository repository;
  @Autowired private ObjectMapper mapper;
  @MockBean private CommonUserRepository commonUserRepository;
  @MockBean private ShopkeeperRepository shopkeeperRepository;

  @BeforeAll
  static void init() {
    initObjects();
  }

  @BeforeEach
  void setUp() {

    Mockito.when(commonUserRepository.findById(PAYER_ID)).thenReturn(Optional.of(payer));
    Mockito.when(shopkeeperRepository.findById(PAYEE_ID)).thenReturn(Optional.of(payee));

    transaction =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void whenCreateReturnSuccess() {
    Response response =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(201, response.getStatusCode()),
        () -> assertNotNull(response.getHeader("Location")));
  }

  @Test
  void whenFindByIdReturnSuccess() throws IOException {

    String id = getId(transaction);

    Response response =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", id)
            .when()
            .get("{id}");

    TransactionDTO responseBody =
        mapper.readValue(response.getBody().asString(), TransactionDTO.class);

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertEquals(UUID.fromString(id), responseBody.getKey()),
        () -> assertEquals(requestBody.getPayee(), responseBody.getPayee()),
        () -> assertEquals(requestBody.getPayer(), responseBody.getPayer()),
        () -> assertEquals(TRANS_VALUE, responseBody.getValue()),
        () -> assertNotNull(responseBody.getTimestamp()));
  }

  private String getId(Response entity) {
    String[] url = entity.getHeader("Location").split("/");
    return url[url.length - 1];
  }

  @NotNull
  private static BigDecimal getFormattedBalance(Double value) {
    return BigDecimal.valueOf(value).setScale(2, RoundingMode.CEILING);
  }

  private static void initObjects() {
    payer = new CommonUser(PAYER_ID, "", "", "", "", PAYER_BALANCE);
    payee = new Shopkeeper(PAYEE_ID, "", "", "", "", PAYEE_BALANCE);
    requestBody = new Transaction(null, PAYER_ID, PAYEE_ID, TRANS_VALUE, null);
  }
}
