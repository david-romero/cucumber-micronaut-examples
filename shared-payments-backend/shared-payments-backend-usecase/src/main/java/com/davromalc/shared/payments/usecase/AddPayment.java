package com.davromalc.shared.payments.usecase;

import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentAdded;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.DomainEventPublisher;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.payments.params.AddPaymentParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import java.time.OffsetDateTime;
import javax.inject.Singleton;

@Singleton
final class AddPayment implements UseCase<AddPaymentParams, Payment> {

  private final PaymentRepository paymentRepository;

  private final DomainEventPublisher eventPublisher;

  AddPayment(PaymentRepository paymentRepository, DomainEventPublisher eventPublisher) {
    this.paymentRepository = paymentRepository;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public Either<Validation, Payment> execute(AddPaymentParams params) {
    final Validation validation = params.validate();
    if (validation.hasErrors()) {
      return validation.asEither();
    } else {
      return Either.<Validation, Payment>right(save(params)).peek(this::publish);
    }
  }

  private Payment save(AddPaymentParams params) {
    return paymentRepository.save(new Payment(
        new User(params.getPayer(), ""),
        new Amount(params.getAmount(), new Currency("€")),
        params.getDescription(),
        params.getDate().orElse(OffsetDateTime.now())
    ));
  }

  private void publish(Payment payment) {
    eventPublisher.publish(new PaymentAdded(payment));
  }
}
