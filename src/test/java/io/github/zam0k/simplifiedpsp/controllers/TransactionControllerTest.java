package io.github.zam0k.simplifiedpsp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(initializers = ContainerInit.class)
@Testcontainers
class TransactionControllerTest {
  public static final BigDecimal TRANS_VALUE =
      BigDecimal.valueOf(10.00).setScale(2, RoundingMode.CEILING);

  public static final BigDecimal PAYER_BALANCE =
      BigDecimal.valueOf(100.00).setScale(2, RoundingMode.CEILING);

  public static final BigDecimal PAYEE_BALANCE =
      BigDecimal.valueOf(100.00).setScale(2, RoundingMode.CEILING);

  private static final ObjectMapper mapper = new ObjectMapper();

  private Transaction requestBody;
  private static CommonUser payer;
  private static Shopkeeper payee;
  private Response transaction;

  @Container private static final MySQL MYSQL_CONTAINER = MySQL.getInstance();
  @Autowired private TransactionRepository repository;
  @Autowired private CommonUserRepository commonUserRepository;
  @Autowired private ShopkeeperRepository shopkeeperRepository;

  @BeforeAll
  static void init() {

    mapper
        .registerModule(new JavaTimeModule())
        .disable(FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(WRITE_DATES_AS_TIMESTAMPS);

    payer =
        new CommonUser(
            null, "Foo Bar", "275.974.380-28", "foobar@gmail.com", "randomPassword", PAYER_BALANCE);
    payee =
        new Shopkeeper(
            null,
            "Foo Bar Inc",
            "94.361.252/0001-90",
            "foobarin@gmail.com",
            "randomPassword",
            PAYEE_BALANCE);
  }

  @BeforeEach
  void setUp() {
    requestBody = new Transaction(null, null, null, TRANS_VALUE, null);
    Response payerResponse =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(payer)
            .when()
            .post();

    Response payeeResponse =
        given()
            .basePath("/api/shopkeepers/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(payee)
            .when()
            .post();

    UUID payeeID = UUID.fromString(getId(payeeResponse));
    UUID payerID = UUID.fromString(getId(payerResponse));

    requestBody.setPayee(payeeID);
    requestBody.setPayer(payerID);

    transaction =
        given()
            .basePath("/api/transactions/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
    commonUserRepository.deleteAll();
    shopkeeperRepository.deleteAll();
  }

  @Test
  void whenCreateReturnSuccess() {
    Response response =
        given()
            .basePath("/api/transactions/v1/")
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
            .basePath("/api/transactions/v1/")
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
}
