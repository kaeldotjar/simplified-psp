package io.github.zam0k.simplifiedpsp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zam0k.simplifiedpsp.configs.TestConfig;
import io.github.zam0k.simplifiedpsp.containers.ContainerInit;
import io.github.zam0k.simplifiedpsp.containers.MySQL;
import io.github.zam0k.simplifiedpsp.domain.CommonUser;
import io.github.zam0k.simplifiedpsp.domain.Transaction;
import io.github.zam0k.simplifiedpsp.repositories.CommonUserRepository;
import io.github.zam0k.simplifiedpsp.repositories.TransactionRepository;
import io.github.zam0k.simplifiedpsp.services.exceptions.handler.ApiError;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@ContextConfiguration(initializers = ContainerInit.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
class CommonUserControllerTest {

  public static final String FULL_NAME = "Foo Bar";
  public static final String CPF = "275.974.380-28";
  public static final String EMAIL = "foobar@gmail.com";
  public static final String PASSWORD = "randomPassword";
  public static final BigDecimal BALANCE =
      BigDecimal.valueOf(100.00).setScale(2, RoundingMode.CEILING);

  private static final String BASE_PATH = "/api/common-users/v1/";
  private static final String PATH_TRANSACTIONS_ID = "{id}/transactions";

  private CommonUser requestBody;
  private String entityId;

  @Container private static final MySQL MYSQL_CONTAINER = MySQL.getInstance();
  @Autowired private ObjectMapper mapper;
  @Autowired private CommonUserRepository repository;
  @MockBean private TransactionRepository transactionRepository;

  @BeforeEach
  void setUp() {
    initCommonUser();
    Response response =
        given()
            .basePath(BASE_PATH)
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
    requestBody.setEmail("email@gmail.com");
    requestBody.setCpf("104.920.880-39");
    Response response =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(requestBody)
            .when()
            .post();

    System.out.println(response.getBody().prettyPrint());

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(201, response.getStatusCode()),
        () -> assertNotNull(response.getHeader("Location")));
  }

  @Test
  void whenFindByIdReturnSuccess() throws IOException {
    Response response =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", entityId)
            .when()
            .get("{id}");

    String content = response.getBody().asString();
    CommonUser responseBody = mapper.readValue(content, CommonUser.class);

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertTrue(content.contains("_links\":{\"self\":{\"href\":")),
        () -> assertEquals(UUID.fromString(entityId), responseBody.getId()),
        () -> assertEquals(FULL_NAME, responseBody.getFullName()),
        () -> assertEquals(EMAIL, responseBody.getEmail()),
        () -> assertEquals(CPF, responseBody.getCpf()),
        () -> assertEquals(BALANCE, responseBody.getBalance()));
  }

  @Test
  void whenGetUserTransactionsReturnSuccess() {
    Transaction transaction =
        new Transaction(
            null, UUID.randomUUID(), UUID.fromString(entityId), BigDecimal.valueOf(50.0), null);

    Mockito.when(transactionRepository.findAllOwnerTransactions(any(), any()))
        .thenReturn(new PageImpl<>(List.of(transaction)));

    Response response =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", entityId)
            .when()
            .get(PATH_TRANSACTIONS_ID);

    String content = response.getBody().asString();

    // TO-DO: better assertions
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals(200, response.getStatusCode()),
        () -> assertNotNull(content));
  }

  @Test
  void whenGetUserTransactionsReturnNoContent() {
    Mockito.when(transactionRepository.findAllOwnerTransactions(any(), any()))
        .thenReturn(new PageImpl<Transaction>(EMPTY_LIST));

    Response response =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", entityId)
            .when()
            .get(PATH_TRANSACTIONS_ID);

    // TO-DO: better assertions
    assertAll(() -> assertNotNull(response), () -> assertEquals(204, response.getStatusCode()));
  }

  @Test
  void whenGetUserTransactionsReturnNotFound() {
    String id = "66a053d3-cc0b-4d99-9a71-abf6a27f2e59";

    Response response =
        given()
            .basePath(BASE_PATH)
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("id", id)
            .when()
            .get(PATH_TRANSACTIONS_ID);

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

  private void initCommonUser() {
    requestBody = new CommonUser(null, FULL_NAME, CPF, EMAIL, PASSWORD, BALANCE);
  }
}
