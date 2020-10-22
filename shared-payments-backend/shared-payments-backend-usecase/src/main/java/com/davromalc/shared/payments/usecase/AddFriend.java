package com.davromalc.shared.payments.usecase;

import com.davromalc.shared.payments.domain.Group;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.UnableToSaveNewFriend;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.shared.DomainEventPublisher;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.domain.user.FriendAdded;
import com.davromalc.shared.payments.usecase.friends.AddFriendParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import java.util.function.UnaryOperator;
import javax.inject.Singleton;

@Singleton
final class AddFriend implements UseCase<AddFriendParams, User> {

  private final GroupRepository groupRepository;

  private final DomainEventPublisher eventPublisher;

  public AddFriend(GroupRepository groupRepository, DomainEventPublisher eventPublisher) {
    this.groupRepository = groupRepository;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public Either<Validation, User> execute(AddFriendParams params) {
    final Validation validation = params.validate();
    if (validation.hasErrors()) {
      return validation.asEither();
    } else {
      final Group group = groupRepository.findGroup(params.getHostId());
      final Group groupWithNewFriend = group.withNewFriend(new User(0, params.getFriendName()));
      return groupRepository.save(groupWithNewFriend).getFriends().stream()
          .filter(user -> user.getName().equals(params.getFriendName()))
          .findFirst()
          .map(publish(groupWithNewFriend))
          .<Either<Validation, User>>map(Either::right)
          .orElseGet(() -> Either.left(new UnableToSaveNewFriend(group, params.getFriendName())));
    }
  }

  private UnaryOperator<User> publish(Group group) {
    return user -> {
      eventPublisher.publish(new FriendAdded(user, group));
      return user;
    };
  }
}
