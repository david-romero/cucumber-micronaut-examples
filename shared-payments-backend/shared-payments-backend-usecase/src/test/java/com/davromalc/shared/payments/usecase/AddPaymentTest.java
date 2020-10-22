package com.davromalc.shared.payments.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentAdded;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.payments.params.AddPaymentParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import com.davromalc.shared.payments.usecase.stubs.InMemoryDomainEventPublisher;
import com.davromalc.shared.payments.usecase.stubs.PaymentRepositoryStub;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AddPaymentTest {

  InMemoryDomainEventPublisher eventPublisher = new InMemoryDomainEventPublisher();

  PaymentRepository paymentRepository = spy(new PaymentRepositoryStub());

  UseCase<AddPaymentParams, Payment> addPayment = new AddPayment(paymentRepository, eventPublisher);

  @Test
  void given_a_new_payment_when_it_is_added_then_it_is_saved() {
    // given
    AddPaymentParams params =
        new AddPaymentParams(3L, BigDecimal.valueOf(35.45), "Supermarket", OffsetDateTime.parse("2020-08-18T10:15:00+02:00"));

    // when
    addPayment.execute(params);

    // then
    verify(paymentRepository, times(1)).save(
        new Payment(new User(3, "David Romero"),
            new Amount(BigDecimal.valueOf(35.45), new Currency("€")),
            "Supermarket", OffsetDateTime.parse("2020-08-18T10:15:00+02:00")));
  }

  @Test
  void given_a_no_valid_payment_when_it_is_added_then_a_validation_error_is_returned() {
    // given
    AddPaymentParams params =
        new AddPaymentParams(null, BigDecimal.valueOf(-10.65), "", OffsetDateTime.parse("2150-09-18T10:15:00+02:00"));

    // when
    final Either<Validation, Payment> newPaymentResponse = addPayment.execute(params);

    // then
    assertThat(newPaymentResponse).isNotNull();
    assertThat(newPaymentResponse.isLeft()).isTrue();
    assertThat(newPaymentResponse.getLeft())
        .isNotNull()
        .extracting(Validation::getErrors)
        .asList()
        .hasSize(4)
        .containsOnlyElementsOf(List.of("The payer cannot be null", "The amount cannot be negative", "The description cannot be blank",
            "The date cannot be located in the future"));
  }

  @Test
  void given_a_new_payment_when_it_is_added_then_a_new_payment_added_event_is_published() {
    // given
    AddPaymentParams params =
        new AddPaymentParams(3L, BigDecimal.valueOf(35.45), "Supermarket", OffsetDateTime.parse("2020-08-18T10:15:00+02:00"));

    // when
    addPayment.execute(params);

    // then
    assertThat(eventPublisher.getEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(PaymentAdded.class);
  }
}