package com.davromalc.shared.payments.usecase.shared.validation;

import static java.util.Optional.of;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class Invalid implements ValidationResult {

  private final Set<String> reasons;

  Invalid(Set<String> reasons) {
    this.reasons = Collections.unmodifiableSet(reasons);
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public Optional<Set<String>> getReasons() {
    return of(reasons);
  }
}
