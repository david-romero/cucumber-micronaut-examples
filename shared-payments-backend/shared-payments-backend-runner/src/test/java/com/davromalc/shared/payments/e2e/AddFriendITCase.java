package com.davromalc.shared.payments.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.infrastructure.rest.dto.FriendDto;
import com.davromalc.shared.payments.infrastructure.rest.dto.UserDto;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.simple.SimpleHttpRequest;
import io.micronaut.test.annotation.MicronautTest;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@MicronautTest(transactional = false)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AddFriendITCase {

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
  HttpClient client;

  @Test
  void given_an_existing_user_and_group_when_a_new_friend_is_added_to_the_group_the_the_new_friend_is_returned() {
    // given
    int userId = 400;
    FriendDto friendDto = new FriendDto("Manu Tenorio");

    // when

    final HttpResponse<UserDto> response =
        client.toBlocking().exchange(new SimpleHttpRequest<>(HttpMethod.POST, "/api/group/user/" + userId, friendDto), UserDto.class);

    // then
    assertThat(response)
        .isNotNull()
        .extracting(HttpResponse::getStatus)
        .isEqualTo(HttpStatus.CREATED);

    assertThat(response.body())
        .isNotNull()
        .matches(dto -> dto.getId() != null && dto.getId() > 0 && "Manu Tenorio".equals(dto.getName()));
  }

  @AfterAll
  static void tearDown() {
    if (postgreSQLContainer != null) {
      postgreSQLContainer.stop();
    }
  }

}
