package io.github.zam0k.simplifiedpsp.controllers;

import io.github.zam0k.simplifiedpsp.configs.TestConfig;
import io.github.zam0k.simplifiedpsp.containers.ContainerInit;
import io.github.zam0k.simplifiedpsp.containers.MySQL;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.CommonUserRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.exceptions.handler.ApiError;
import io.restassured.path.json.JsonPath;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(initializers = ContainerInit.class)
@Testcontainers
class CommonUserControllerTest {

  public static final String FULL_NAME = "Foo Bar";
  public static final String CPF = "275.974.380-28";
  public static final String EMAIL = "foobar@gmail.com";
  public static final String PASSWORD = "randomPassword";
  public static final BigDecimal BALANCE =
      BigDecimal.valueOf(100.00).setScale(2, RoundingMode.CEILING);

  private CommonUser requestBody;
  private static final ObjectMapper mapper = new ObjectMapper();
  @Container private static final MySQL MYSQL_CONTAINER = MySQL.getInstance();

  @Autowired private CommonUserRepository repository;
  @Autowired private TransactionRepository transactionRepository;

  @BeforeAll
  static void init() {
    mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @BeforeEach
  void setUp() {
    requestBody = new CommonUser(null, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Test
  void whenCreateReturnSuccess() {
    Response response =
        given()
            .basePath("/api/common-users/v1/")
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
    Response entity =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    String id = getEntityId(entity);

    Response response =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", id)
            .when()
            .get("{id}");

    String content = response.getBody().asString();
    CommonUser responseBody = mapper.readValue(content, CommonUser.class);

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertTrue(content.contains("_links\":{\"self\":{\"href\":")),
        () -> assertEquals(UUID.fromString(id), responseBody.getId()),
        () -> assertEquals(FULL_NAME, responseBody.getFullName()),
        () -> assertEquals(EMAIL, responseBody.getEmail()),
        () -> assertEquals(CPF, responseBody.getCpf()),
        () -> assertEquals(BALANCE, responseBody.getBalance()));
  }

  @Test
  void whenGetUserTransactionsReturnSuccess() {
    Response payee =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    UUID payeeId = UUID.fromString(getEntityId(payee));

    requestBody.setEmail("anotherEmail@gmail.com");
    requestBody.setCpf("192.169.600-19");

    Response payer =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    UUID payerId = UUID.fromString(getEntityId(payer));

    Transaction transaction =
        new Transaction(null, payerId, payeeId, BigDecimal.valueOf(50.00), LocalDateTime.now());

    transactionRepository.save(transaction);

    Response response =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", payeeId)
            .when()
            .get("{id}/transactions");

    String content = response.getBody().asString();
    JsonPath jsonPath = JsonPath.from(content);
    Map<String, String> transactionJson =
        jsonPath.getMap("_embedded.transactions[0]", String.class, String.class);

    response.body().asString();

    // TO-DO: better assertions
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertNotNull(content),
        () -> assertNotNull(transactionJson.get("_links")));
  }

  @Test
  void whenGetUserTransactionsReturnNoContent() {
    Response entity =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    String id = getEntityId(entity);

    Response response =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", id)
            .when()
            .get("{id}/transactions");

    // TO-DO: better assertions
    assertAll(() -> assertNotNull(response), () -> assertEquals(204, response.getStatusCode()));
  }

  @Test
  void whenGetUserTransactionsReturnNotFound() {
    String id = "66a053d3-cc0b-4d99-9a71-abf6a27f2e59";

    Response response =
        given()
            .basePath("/api/common-users/v1/")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", id)
            .when()
            .get("{id}/transactions");

    ApiError error = response.getBody().as(ApiError.class);

    // TO-DO: better assertions
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(404, response.getStatusCode()),
        () -> assertEquals("Object cannot be found", error.getError().get(0)),
        () -> assertNotNull(error.getTimestamp()),
        () -> assertNotNull(error.getPath()));
  }

  private String getEntityId(Response entity) {
    String[] url = entity.getHeader("Location").split("/");
    return url[url.length - 1];
  }
}
