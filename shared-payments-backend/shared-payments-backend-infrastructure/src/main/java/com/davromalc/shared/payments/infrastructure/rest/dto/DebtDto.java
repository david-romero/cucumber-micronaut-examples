package com.davromalc.shared.payments.infrastructure.rest.dto;

import java.io.Serializable;
import java.util.Objects;

public class DebtDto implements Serializable {

  private UserDto payer;

  private UserDto recipient;

  private String amount;

  public DebtDto(UserDto payer, UserDto recipient, String amount) {
    this.payer = payer;
    this.recipient = recipient;
    this.amount = amount;
  }

  public DebtDto() {
  }

  public UserDto getPayer() {
    return payer;
  }

  public void setPayer(UserDto payer) {
    this.payer = payer;
  }

  public UserDto getRecipient() {
    return recipient;
  }

  public void setRecipient(UserDto recipient) {
    this.recipient = recipient;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DebtDto debtDto = (DebtDto) o;
    return Objects.equals(payer, debtDto.payer) &&
        Objects.equals(recipient, debtDto.recipient) &&
        Objects.equals(amount, debtDto.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payer, recipient, amount);
  }

  @Override
  public String toString() {
    return "DebtDto{" +
        "payer=" + payer +
        ", recipient=" + recipient +
        ", amount='" + amount + '\'' +
        '}';
  }
}
