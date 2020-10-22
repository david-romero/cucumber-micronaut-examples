package com.davromalc.shared.payments.domain.payment;

import com.davromalc.shared.payments.domain.shared.DomainEvent;
import java.time.Instant;
import java.util.Objects;

public final class PaymentAdded extends DomainEvent {

  private final Payment payment;

  public PaymentAdded(Payment payment) {
    super(Long.toString(payment.getPayer().getId()), Instant.now());
    this.payment = payment;
  }

  public Payment getPayment() {
    return payment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    PaymentAdded that = (PaymentAdded) o;
    return Objects.equals(payment, that.payment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), payment);
  }

  @Override
  public String toString() {
    return "PaymentAdded{" +
        "payment=" + payment +
        "} " + super.toString();
  }
}
