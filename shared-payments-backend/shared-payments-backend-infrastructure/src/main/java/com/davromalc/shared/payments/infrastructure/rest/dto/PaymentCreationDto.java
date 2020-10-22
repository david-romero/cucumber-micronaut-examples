package com.davromalc.shared.payments.infrastructure.rest.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class PaymentCreationDto implements Serializable {

  @NotNull
  private Double amount;

  @NotNull
  private String description;

  private OffsetDateTime date;

  public PaymentCreationDto(@NotNull Double amount, @NotNull String description, OffsetDateTime date) {
    this.amount = amount;
    this.description = description;
    this.date = date;
  }

  public PaymentCreationDto() {
  }

  public Double getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentCreationDto that = (PaymentCreationDto) o;
    return Objects.equals(amount, that.amount) &&
        Objects.equals(description, that.description) &&
        Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, description, date);
  }

  @Override
  public String toString() {
    return "PaymentCreationDto{" +
        "amount=" + amount +
        ", description='" + description + '\'' +
        ", date=" + date +
        '}';
  }
}
