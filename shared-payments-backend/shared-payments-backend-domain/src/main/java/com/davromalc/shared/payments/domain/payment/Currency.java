package com.davromalc.shared.payments.domain.payment;

import java.util.Objects;

public final class Currency {

  private final String symbol;

  public Currency(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Currency currency = (Currency) o;
    return Objects.equals(symbol, currency.symbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol);
  }

  @Override
  public String toString() {
    return "Currency{"
        + "symbol='" + symbol + '\''
        + '}';
  }
}
