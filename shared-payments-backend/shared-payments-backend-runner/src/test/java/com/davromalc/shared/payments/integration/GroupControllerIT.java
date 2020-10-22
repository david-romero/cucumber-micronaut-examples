package com.davromalc.shared.payments.integration;

import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.usecase.payments.params.ShowSharedPaymentsParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MockBean;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class GroupControllerIT {

  private static EmbeddedServer server;

  private static HttpClient client;

  @BeforeAll
  public static void setupServer() {
    server = ApplicationContext.run(EmbeddedServer.class, "integration");
    client = server
        .getApplicationContext()
        .createBean(HttpClient.class, server.getURL());
  }

  @AfterAll
  public static void stopServer() {
    if (server != null) {
      server.stop();
    }
    if (client != null) {
      client.stop();
    }
  }

  @Test
    //FIXME Integration tests with Controller is not working
  void given_two_payments_when_are_retrieved_then_all_fields_are_present() {
    // given
    var userId = 3;

    // when
    List<Map<String, ?>> result =
        client.toBlocking().retrieve(HttpRequest.GET("/api/payment/group/user/" + userId), List.class);

    // then
  }

  @MockBean(value = UseCase.class, named = "showSharedPayments")
  @Requires(env = "integration")
  @Primary
  UseCase<ShowSharedPaymentsParams, List<Payment>> showSharedPayments() {
    return Mockito.mock(UseCase.class);
  }

}
