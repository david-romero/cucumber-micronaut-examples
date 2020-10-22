package com.davromalc.shared.payments.domain.shared;

import java.util.List;

public interface Validation {

  List<String> getErrors();

  boolean hasErrors();

  <T> Either<Validation, T> asEither();
}
