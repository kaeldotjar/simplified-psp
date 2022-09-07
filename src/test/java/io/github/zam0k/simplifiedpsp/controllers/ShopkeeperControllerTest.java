package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.configs.TestConfig;
import io.github.zam0k.simplifiedpsp.containers.ContainerInit;
import io.github.zam0k.simplifiedpsp.containers.MySQL;
import io.github.zam0k.simplifiedpsp.domain.Shopkeeper;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.ShopkeeperRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(initializers = ContainerInit.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
class ShopkeeperControllerTest {
  public static final String FULL_NAME = "subject name";
  public static final String CNPJ = "68.340.815/0001-53";
  public static final String EMAIL = "subject@gmail.com";
  public static final String PASSWORD = "123456";
  public static final BigDecimal BALANCE =
      BigDecimal.valueOf(100.00).setScale(2, RoundingMode.CEILING);
  private static final ObjectMapper mapper = new ObjectMapper();

  private Shopkeeper requestBody;
  private String entityId;

  @Container private static final MySQL MYSQL_CONTAINER = MySQL.getInstance();
  @MockBean private TransactionRepository transactionRepository;
  @Autowired private ShopkeeperRepository repository;

  @BeforeAll
  static void init() {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @BeforeEach
  void setUp() {
    requestBody = new Shopkeeper(null, FULL_NAME, CNPJ, EMAIL, PASSWORD, BALANCE);
    Response response =
        given()
            .basePath("/api/shopkeepers/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    entityId = getEntityId(response);
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void whenCreateReturnSuccess() {
    requestBody.setEmail("newEmail@gmail.com");
    requestBody.setCnpj("05.322.807/0001-64");
    Response response =
        given()
            .basePath("/api/shopkeepers/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    System.out.println(response.prettyPrint());

    Assertions.assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(201, response.getStatusCode()),
        () -> assertNotNull(response.getHeader("Location")));
  }

  @Test
  void whenFindAllReturnSuccess() {
    Response response =
        given()
            .basePath("/api/shopkeepers/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .when()
            .get();

    String content = response.getBody().asString();

    System.out.println(content);

    Assertions.assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertNotNull(response.getBody()));
  }

  @Test
  void whenFindByIdReturnSuccess() throws IOException {
    Response response =
        given()
            .basePath("/api/shopkeepers/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", entityId)
            .when()
            .get("{id}");

    String content = response.getBody().asString();
    Shopkeeper responseBody = mapper.readValue(content, Shopkeeper.class);

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertEquals(UUID.fromString(entityId), responseBody.getId()),
        () -> assertEquals(FULL_NAME, responseBody.getFullName()),
        () -> assertEquals(EMAIL, responseBody.getEmail()),
        () -> assertEquals(CNPJ, responseBody.getCnpj()),
        () -> assertEquals(BALANCE, responseBody.getBalance()));
  }

  @Test
  void whenGetUserTransactionsReturnSuccess() {

    Transaction transaction =
        new Transaction(
            null, UUID.fromString(entityId), UUID.randomUUID(), BigDecimal.valueOf(50.0), null);

    Mockito.when(transactionRepository.findAllByPayee(any(), any()))
        .thenReturn(new PageImpl<>(List.of(transaction)));

    requestBody.setEmail("anotherEmail@gmail.com");
    requestBody.setCnpj("192.169.600-19");

    Response response =
        given()
            .basePath("/api/shopkeepers/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", entityId)
            .when()
            .get("{id}/transactions");

    String content = response.getBody().asString();

    System.out.println(response.getBody().prettyPrint());

    System.out.println(content);

    // TO-DO: better assertions
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertNotNull(content));
  }

  private String getEntityId(Response entity) {
    String[] url = entity.getHeader("Location").split("/");
    return url[url.length - 1];
  }
}
