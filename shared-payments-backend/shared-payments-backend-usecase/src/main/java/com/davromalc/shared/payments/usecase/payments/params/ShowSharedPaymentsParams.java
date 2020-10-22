package com.davromalc.shared.payments.usecase.payments.params;

import static com.davromalc.shared.payments.usecase.shared.ParametersValidation.empty;

import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.shared.ParametersValidation;
import com.davromalc.shared.payments.usecase.shared.UseCaseParams;
import java.util.List;
import java.util.Objects;

public final class ShowSharedPaymentsParams implements UseCaseParams {

  private final Long userId;

  public ShowSharedPaymentsParams(Long userId) {
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  @Override
  public Validation validate() {
    if (userId == null) {
      return new ParametersValidation(List.of("The user identifier cannot be null"));
    }
    return empty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShowSharedPaymentsParams that = (ShowSharedPaymentsParams) o;
    return Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

  @Override
  public String toString() {
    return "ShowSharedPaymentsParams{"
        + "userId=" + userId
        + '}';
  }
}
