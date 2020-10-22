package com.davromalc.shared.payments.usecase.shared.validation;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface ValidationResult {

  static ValidationResult valid() {
    return ValidationSupport.valid();
  }

  static ValidationResult invalid(Set<String> reasons) {
    return new Invalid(reasons);
  }

  static ValidationResult invalid(String reason, String... reasons) {
    final Set<String> reasonList = new HashSet<>();
    reasonList.add(reason);
    reasonList.addAll(asList(reasons));
    return new Invalid(reasonList);
  }

  boolean isValid();

  Optional<Set<String>> getReasons();

}
