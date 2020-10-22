package com.davromalc.shared.payments.infrastructure.rest.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class BalanceDto implements Serializable {

  private Map<String, String> amounts;

  public BalanceDto(Map<String, String> amounts) {
    this.amounts = amounts;
  }

  public BalanceDto() {
  }

  public Map<String, String> getAmounts() {
    return Collections.unmodifiableMap(amounts);
  }

  public void setAmounts(Map<String, String> amounts) {
    this.amounts = Collections.unmodifiableMap(amounts);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BalanceDto that = (BalanceDto) o;
    return Objects.equals(amounts, that.amounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amounts);
  }

  @Override
  public String toString() {
    return "BalanceDto{" +
        "amounts=" + amounts +
        '}';
  }
}
