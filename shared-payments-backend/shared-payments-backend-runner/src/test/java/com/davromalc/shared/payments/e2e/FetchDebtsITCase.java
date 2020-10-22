package com.davromalc.shared.payments.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.simple.SimpleHttpRequest;
import io.micronaut.test.annotation.MicronautTest;
import java.util.LinkedHashMap;
import java.util.List;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@MicronautTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FetchDebtsITCase {

  static GenericContainer<?> postgreSQLContainer;

  static {
    postgreSQLContainer = new PostgreSQLContainer<>("postgres")
        .withDatabaseName("shared_payments")
        .withUsername("custom_user")
        .withPassword("magical_password")
        .withClasspathResourceMapping("scripts/schema.sql", "/docker-entrypoint-initdb.d/schema.sql", BindMode.READ_WRITE);
    postgreSQLContainer.start();
  }

  @Inject
  @Client("/")
  RxHttpClient client;

  @Test
  void given_an_existing_user_and_group_when_the_payments_are_shown_then_debts_from_group_are_retrieved() {
    // given
    int userId = 100;

    // when
    final HttpResponse<List> response =
        client.toBlocking()
            .exchange(new SimpleHttpRequest<>(HttpMethod.GET, "/api/payment/user/" + userId + "/debts", null), List.class);

    // then
    assertThat(response)
        .isNotNull()
        .extracting(HttpResponse::getStatus)
        .isEqualTo(HttpStatus.OK);

    assertThat(response.body()).isNotNull();
    List<LinkedHashMap<String, ?>> result = response.body();
    assertThat(result)
        .isNotNull()
        .hasSize(2)
        .contains(debtFromOneToTwo())
        .contains(debtFromDavidToTwo());
  }

  LinkedHashMap<String, ?> debtFromOneToTwo() {
    LinkedHashMap<String, Object> debt = new LinkedHashMap<>();
    LinkedHashMap<String, Object> payer = new LinkedHashMap<>();
    payer.put("id", 300);
    payer.put("name", "User 1");
    debt.put("payer", payer);
    LinkedHashMap<String, Object> recipient = new LinkedHashMap<>();
    recipient.put("id", 200);
    recipient.put("name", "User 2");
    debt.put("recipient", recipient);
    debt.put("amount", "21.87 €");
    return debt;
  }

  LinkedHashMap<String, ?> debtFromDavidToTwo() {
    LinkedHashMap<String, Object> debt = new LinkedHashMap<>();
    LinkedHashMap<String, Object> payer = new LinkedHashMap<>();
    payer.put("id", 100);
    payer.put("name", "David Romero");
    debt.put("payer", payer);
    LinkedHashMap<String, Object> recipient = new LinkedHashMap<>();
    recipient.put("id", 200);
    recipient.put("name", "User 2");
    debt.put("recipient", recipient);
    debt.put("amount", "1.87 €");
    return debt;
  }

  @AfterAll
  static void tearDown() {
    if (postgreSQLContainer != null) {
      postgreSQLContainer.stop();
    }
  }

}
