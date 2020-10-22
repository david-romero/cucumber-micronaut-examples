package com.davromalc.shared.payments.usecase.shared;

import static java.util.Collections.unmodifiableList;

import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ParametersValidation extends RuntimeException implements Validation {

  private static final ParametersValidation EMPTY = new ParametersValidation();

  private final List<String> errors;

  public ParametersValidation() {
    this.errors = new ArrayList<>();
  }

  public ParametersValidation(List<String> errors) {
    super(String.join(", ", errors));
    this.errors = Collections.unmodifiableList(errors);
  }

  public static ParametersValidation empty() {
    return EMPTY;
  }

  @Override
  public List<String> getErrors() {
    return unmodifiableList(errors);
  }

  public void addError(String error) {
    this.errors.add(error);
  }

  @Override
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  public void addErrors(Set<String> errors) {
    this.errors.addAll(errors);
  }

  @Override
  public <T> Either<Validation, T> asEither() {
    return Either.left(this);
  }

  @Override
  public String toString() {
    return "ParametersValidation{" + "errors=" + String.join(",", errors) + '}';
  }

  @Override
  public String getMessage() {
    return String.join(", ", getErrors());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParametersValidation that = (ParametersValidation) o;
    return Objects.equals(errors, that.errors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errors);
  }
}
