package com.davromalc.shared.payments.domain;

import com.davromalc.shared.payments.domain.payment.Amount;
import java.util.Objects;

public final class Debt {

  private final User payer;

  private final User recipient;

  private final Amount amount;

  public Debt(User payer, User recipient, Amount amount) {
    this.payer = payer;
    this.recipient = recipient;
    this.amount = amount;
  }

  public User getPayer() {
    return payer;
  }

  public User getRecipient() {
    return recipient;
  }

  public Amount getAmount() {
    return amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Debt debt = (Debt) o;
    return Objects.equals(payer, debt.payer) &&
        Objects.equals(recipient, debt.recipient) &&
        Objects.equals(amount, debt.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payer, recipient, amount);
  }

  @Override
  public String toString() {
    return "Debt{" +
        "payer=" + payer +
        ", recipient=" + recipient +
        ", amount=" + amount +
        '}';
  }
}
