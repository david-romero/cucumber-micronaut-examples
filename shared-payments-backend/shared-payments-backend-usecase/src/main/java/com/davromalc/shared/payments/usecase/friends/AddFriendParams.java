package com.davromalc.shared.payments.usecase.friends;

import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.shared.ParametersValidation;
import com.davromalc.shared.payments.usecase.shared.UseCaseParams;

public final class AddFriendParams implements UseCaseParams {

  private final String friendName;

  private final Long hostId;

  public AddFriendParams(String friendName, Long hostId) {
    this.friendName = friendName;
    this.hostId = hostId;
  }

  public String getFriendName() {
    return friendName;
  }

  public Long getHostId() {
    return hostId;
  }

  @Override
  public Validation validate() {
    final ParametersValidation validation = new ParametersValidation();
    if (friendName == null || friendName.isBlank()) {
      validation.addError("The name of the friend cannot be blank");
    }
    if (hostId == null) {
      validation.addError("A new friend needs a valid host");
    }
    return validation;
  }
}
