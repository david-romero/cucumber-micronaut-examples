package com.davromalc.shared.payments.domain;

import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import java.util.List;

public class UnableToSaveNewFriend extends RuntimeException implements Validation {

  public UnableToSaveNewFriend(Group group, String newFriendName) {
    super("Unable to add the friend " + newFriendName + " to the group " + group);
  }

  @Override
  public List<String> getErrors() {
    return List.of(getMessage());
  }

  @Override
  public boolean hasErrors() {
    return true;
  }

  @Override
  public <T> Either<Validation, T> asEither() {
    return Either.left(this);
  }
}
