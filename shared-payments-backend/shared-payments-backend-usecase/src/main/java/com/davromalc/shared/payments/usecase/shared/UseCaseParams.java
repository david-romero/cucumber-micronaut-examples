package com.davromalc.shared.payments.usecase.shared;

import com.davromalc.shared.payments.domain.shared.Validation;

public interface UseCaseParams {

  default Validation validate() {
    return ParametersValidation.empty();
  }

}
