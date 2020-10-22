package com.davromalc.shared.payments.acceptance.config;

import com.davromalc.shared.payments.domain.Balance;
import com.davromalc.shared.payments.domain.Debt;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class World {

  private Either<Validation, List<Payment>> payments;

  private Long userId;

  private Either<Validation, Payment> payment;

  private Either<Validation, Balance> balance;

  private Either<Validation, List<Debt>> debts;

  public void setPayments(Either<Validation, List<Payment>> payments) {
    this.payments = payments;
  }

  public Either<Validation, List<Payment>> getPayments() {
    return payments;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setPayment(Either<Validation, Payment> payment) {
    this.payment = payment;
  }

  public Either<Validation, Payment> getPayment() {
    return payment;
  }

  public void setBalance(Either<Validation, Balance> balance) {
    this.balance = balance;
  }

  public Either<Validation, Balance> getBalance() {
    return balance;
  }

  public void setDebts(Either<Validation, List<Debt>> debts) {
    this.debts = debts;
  }

  public Either<Validation, List<Debt>> getDebts() {
    return debts;
  }

  public void reset() {
    payments = null;
    userId = null;
    payment = null;
    balance = null;
    debts = null;
  }
}
