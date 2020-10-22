package com.davromalc.shared.payments.domain.user;

import com.davromalc.shared.payments.domain.Group;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.shared.DomainEvent;
import java.time.Instant;

public final class FriendAdded extends DomainEvent {

  private final Group group;

  public FriendAdded(User user, Group group) {
    super(Long.toString(user.getId()), Instant.now());
    this.group = group;
  }

  public Group getGroup() {
    return group;
  }

  @Override
  public String toString() {
    return "FriendAdded{" +
        "group=" + group +
        "} " + super.toString();
  }
}
