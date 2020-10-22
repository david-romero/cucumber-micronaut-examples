package com.davromalc.shared.payments.usecase.payments.params;

import static com.davromalc.shared.payments.usecase.payments.params.validation.AddPaymentParamsValidation.fullParametersValidations;

import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.shared.ParametersValidation;
import com.davromalc.shared.payments.usecase.shared.UseCaseParams;
import com.davromalc.shared.payments.usecase.shared.validation.ValidationResult;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

public class AddPaymentParams implements UseCaseParams {

  private final Long payer;

  private final BigDecimal amount;

  private final String description;

  private final Optional<OffsetDateTime> date;

  public AddPaymentParams(Long payer, BigDecimal amount, String description, OffsetDateTime date) {
    this.payer = payer;
    this.amount = amount;
    this.description = description;
    this.date = Optional.ofNullable(date);
  }

  public Long getPayer() {
    return payer;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }

  public Optional<OffsetDateTime> getDate() {
    return date;
  }

  @Override
  public Validation validate() {
    final ParametersValidation validation = new ParametersValidation();
    final ValidationResult result = fullParametersValidations().apply(this);
    result.getReasons().ifPresent(validation::addErrors);
    return validation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddPaymentParams that = (AddPaymentParams) o;
    return Objects.equals(payer, that.payer) &&
        Objects.equals(amount, that.amount) &&
        Objects.equals(description, that.description) &&
        Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payer, amount, description, date);
  }

  @Override
  public String toString() {
    return "AddPaymentParams{" +
        "payer=" + payer +
        ", amount=" + amount +
        ", description='" + description + '\'' +
        ", date=" + date +
        '}';
  }
}
