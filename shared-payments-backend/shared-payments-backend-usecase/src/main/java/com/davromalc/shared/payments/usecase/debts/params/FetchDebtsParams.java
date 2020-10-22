package com.davromalc.shared.payments.usecase.debts.params;

import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.shared.ParametersValidation;
import com.davromalc.shared.payments.usecase.shared.UseCaseParams;
import java.util.Objects;

public final class FetchDebtsParams implements UseCaseParams {

  private final Long userId;

  public FetchDebtsParams(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  @Override
  public Validation validate() {
    final ParametersValidation validation = new ParametersValidation();
    if (userId == null) {
      validation.addError("The user identifier cannot be null");
    }
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
    FetchDebtsParams that = (FetchDebtsParams) o;
    return Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

  @Override
  public String toString() {
    return "FetchDebtsParams{" +
        "userId=" + userId +
        '}';
  }
}
