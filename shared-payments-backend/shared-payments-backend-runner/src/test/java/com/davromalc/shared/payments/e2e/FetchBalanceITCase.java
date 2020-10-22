package com.davromalc.shared.payments.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.infrastructure.rest.dto.BalanceDto;
import com.davromalc.shared.payments.infrastructure.rest.dto.PaymentDto;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.simple.SimpleHttpRequest;
import io.micronaut.test.annotation.MicronautTest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
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
public class FetchBalanceITCase {

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
  void given_an_existing_user_and_group_when_the_payments_are_shown_then_payments_from_group_are_retrieved() {
    // given
    int userId = 100;

    // when
    final HttpResponse<BalanceDto> response =
        client.toBlocking()
            .exchange(new SimpleHttpRequest<>(HttpMethod.GET, "/api/payment/user/" + userId + "/balance", null), BalanceDto.class);

    // then
    assertThat(response)
        .isNotNull()
        .extracting(HttpResponse::getStatus)
        .isEqualTo(HttpStatus.OK);

    assertThat(response.body()).isNotNull();
    assertThat(response.body().getAmounts())
        .isNotNull()
        .containsEntry("David Romero", "-1.87 €")
        .containsEntry("User 1", "-21.87 €")
        .containsEntry("User 2", "23.74 €");
  }

  @AfterAll
  static void tearDown() {
    if (postgreSQLContainer != null) {
      postgreSQLContainer.stop();
    }
  }

}
