package com.davromalc.shared.payments.integration.rest;

import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.usecase.payments.params.ShowSharedPaymentsParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import java.util.List;
import org.mockito.Mockito;

@Requires(env = "integration")
public class Config {

  @Bean()
  @Primary
  UseCase<ShowSharedPaymentsParams, List<Payment>> showSharedPayments() {
    return Mockito.mock(UseCase.class);
  }

}
