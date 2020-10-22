package com.davromalc.shared.payments.domain.payment;

import com.davromalc.shared.payments.domain.User;
import java.time.OffsetDateTime;
import java.util.Objects;

public final class Payment {

  private final User payer;

  private final Amount amount;

  private final String description;

  private final OffsetDateTime date;

  public Payment(User payer, Amount amount, String description, OffsetDateTime date) {
    this.payer = payer;
    this.amount = amount;
    this.description = description;
    this.date = date;
  }

  public User getPayer() {
    return payer;
  }

  public Amount getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public Payment withPayer(User payer) {
    return new Payment(payer, amount, description, date);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Payment payment = (Payment) o;
    return Objects.equals(payer, payment.payer) &&
        Objects.equals(amount, payment.amount) &&
        Objects.equals(description, payment.description) &&
        Objects.equals(date, payment.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payer, amount, description, date);
  }

  @Override
  public String toString() {
    return "Payment{"
        + "payer=" + payer
        + ", amount=" + amount
        + ", description='" + description + '\''
        + ", date=" + date
        + '}';
  }
}
