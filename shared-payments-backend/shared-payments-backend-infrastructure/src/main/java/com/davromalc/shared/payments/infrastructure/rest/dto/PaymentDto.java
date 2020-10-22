package com.davromalc.shared.payments.infrastructure.rest.dto;

import java.io.Serializable;
import java.util.Objects;

public class PaymentDto implements Serializable {

  private String description;

  private String amount;

  private String date;

  private String user;

  public PaymentDto() {
  }

  public PaymentDto(String description, String amount, String date, String user) {
    this.description = description;
    this.amount = amount;
    this.date = date;
    this.user = user;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentDto that = (PaymentDto) o;
    return Objects.equals(description, that.description) &&
        Objects.equals(amount, that.amount) &&
        Objects.equals(date, that.date) &&
        Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, amount, date, user);
  }

  @Override
  public String toString() {
    return "PaymentDto{"
        + "description='" + description + '\''
        + ", amount='" + amount + '\''
        + ", date='" + date + '\''
        + ", user='" + user + '\''
        + '}';
  }
}
