package com.davromalc.shared.payments.domain;

import com.davromalc.shared.payments.domain.payment.Amount;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class Balance {

  private final Map<User, Amount> amount;

  public Balance(Map<User, Amount> amount) {
    this.amount = Collections.unmodifiableMap(amount);
  }

  public Map<User, Amount> getAmount() {
    return Collections.unmodifiableMap(amount);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Balance balance = (Balance) o;
    return Objects.equals(amount, balance.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }

  @Override
  public String toString() {
    return "Balance{" +
        "amount=" + amount +
        '}';
  }
}
