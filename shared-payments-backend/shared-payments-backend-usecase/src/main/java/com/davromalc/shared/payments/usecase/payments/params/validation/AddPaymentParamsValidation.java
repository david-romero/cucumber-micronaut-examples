package com.davromalc.shared.payments.usecase.payments.params.validation;

import static com.davromalc.shared.payments.usecase.shared.validation.ValidationResult.invalid;
import static com.davromalc.shared.payments.usecase.shared.validation.ValidationResult.valid;

import com.davromalc.shared.payments.usecase.payments.params.AddPaymentParams;
import com.davromalc.shared.payments.usecase.shared.validation.ValidationResult;
import com.davromalc.shared.payments.usecase.shared.validation.ValidationSupport;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Predicate;

public interface AddPaymentParamsValidation extends Function<AddPaymentParams, ValidationResult> {

  static AddPaymentParamsValidation payerIsPresent() {
    return holds(params -> params.getPayer() != null, "The payer cannot be null");
  }

  static AddPaymentParamsValidation amountCannotBeNegative() {
    return holds(params -> params.getAmount() != null && BigDecimal.ZERO.compareTo(params.getAmount()) < 0,
        "The amount cannot be negative");
  }

  static AddPaymentParamsValidation descriptionIsNotBlank() {
    return holds(params -> params.getDescription() != null && !params.getDescription().isBlank(), "The description cannot be blank");
  }

  static AddPaymentParamsValidation dateIsInTheFuture() {
    return holds(params -> params.getDate().orElse(OffsetDateTime.now()).isBefore(OffsetDateTime.now().plusSeconds(1)), "The date cannot be located in the future");
  }


  static AddPaymentParamsValidation holds(Predicate<AddPaymentParams> p, String message) {
    return params -> p.test(params) ? valid() : invalid(message);
  }

  static AddPaymentParamsValidation fullParametersValidations() {
    return payerIsPresent()
        .and(amountCannotBeNegative())
        .and(descriptionIsNotBlank())
        .and(dateIsInTheFuture());
  }

  default AddPaymentParamsValidation and(AddPaymentParamsValidation other) {
    return params -> ValidationSupport.zip(this.apply(params), other.apply(params));
  }
}
