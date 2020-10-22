package com.davromalc.shared.payments.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.annotation.MicronautTest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@MicronautTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SharedPaymentsITCase {

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
  EmbeddedApplication<?> application;

  @Inject
  @Client("/")
  RxHttpClient client;

  @Test
  void verify_the_app_is_running() {
    Assertions.assertTrue(application.isRunning());
  }

  @Test
  void given_an_existing_user_and_group_when_the_payments_are_shown_then_payments_from_group_are_retrieved() {
    // given
    int userId = 300;

    // when
    List<LinkedHashMap<String, ?>> result =
        client.toBlocking().retrieve(HttpRequest.GET("/api/payment/group/user/" + userId), List.class);

    // then
    assertThat(result)
        .isNotNull()
        .hasSize(2)
        .allMatch(Predicate.not(Objects::isNull));
  }

  @AfterAll
  static void tearDown() {
    if (postgreSQLContainer != null) {
      postgreSQLContainer.stop();
    }
  }

}
