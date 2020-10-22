package com.davromalc.shared.payments.domain.payment;

import java.math.BigDecimal;
import java.util.Objects;

public final class Amount {

  private final BigDecimal amount;

  private final Currency currency;

  public Amount(BigDecimal amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Amount amount1 = (Amount) o;
    return Objects.equals(amount, amount1.amount) &&
        Objects.equals(currency, amount1.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public String toString() {
    return "Amount{"
        + "amount=" + amount
        + ", currency=" + currency
        + '}';
  }
}
