package com.davromalc.shared.payments.usecase.shared;

import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;

@FunctionalInterface
public interface UseCase<T extends UseCaseParams, R> {

  Either<Validation, R> execute(T params);
}
