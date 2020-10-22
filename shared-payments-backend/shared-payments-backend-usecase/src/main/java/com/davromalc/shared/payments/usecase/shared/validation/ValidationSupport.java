package com.davromalc.shared.payments.usecase.shared.validation;

import static com.davromalc.shared.payments.usecase.shared.validation.ValidationResult.invalid;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationSupport {

  private static final ValidationResult valid = new ValidationResult() {
    public boolean isValid() {
      return true;
    }

    public Optional<Set<String>> getReasons() {
      return Optional.empty();
    }
  };

  private ValidationSupport() {
  }

  static ValidationResult valid() {
    return valid;
  }

  public static ValidationResult zip(ValidationResult left, ValidationResult right) {
    if (left.isValid() && right.isValid()) {
      return valid();
    } else {
      return invalid(concat(left.getReasons(), right.getReasons()).flatMap(Collection::stream).collect(Collectors.toSet()));
    }
  }

  public static Stream<Set<String>> concat(Optional<Set<String>> leftReasons, Optional<Set<String>> rightReason) {
    return Stream.concat(toStream(leftReasons), toStream(rightReason));
  }

  public static <T> Stream<T> toStream(Optional<T> optional) {
    return optional.map(Stream::of).orElseGet(Stream::empty);
  }

}
