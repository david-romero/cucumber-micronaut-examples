package com.davromalc.shared.payments.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.domain.user.FriendAdded;
import com.davromalc.shared.payments.usecase.friends.AddFriendParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import com.davromalc.shared.payments.usecase.stubs.GroupRepositoryStub;
import com.davromalc.shared.payments.usecase.stubs.InMemoryDomainEventPublisher;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AddFriendTest {

  InMemoryDomainEventPublisher eventPublisher = new InMemoryDomainEventPublisher();

  GroupRepository groupRepository = new GroupRepositoryStub();

  UseCase<AddFriendParams, User> addFriend = new AddFriend(groupRepository, eventPublisher);

  @Test
  void given_an_existing_user_belonging_to_a_group_when_a_new_friend_is_added_then_new_friend_is_returned() {
    // given
    var newFriendName = "Paco Gandia";
    var hostId = 3L;
    final AddFriendParams addFriendParams = new AddFriendParams(newFriendName, hostId);

    // when
    final Either<Validation, User> newFriendResponse = addFriend.execute(addFriendParams);

    // then
    assertThat(newFriendResponse).isNotNull();
    assertThat(newFriendResponse.isRight()).isTrue();
    assertThat(newFriendResponse.get())
        .isNotNull()
        .extracting(User::getName)
        .isEqualTo(newFriendName);
  }

  @Test
  void given_an_existing_user_belonging_to_a_group_when_a_new_friend_is_added_then_a_new_friend_is_saved() {
    // given
    var newFriendName = "Paco Gandia";
    var hostId = 3L;
    final AddFriendParams addFriendParams = new AddFriendParams(newFriendName, hostId);

    // when
    addFriend.execute(addFriendParams);

    // then
    assertThat(groupRepository.findGroup(hostId).getFriends())
        .extracting(User::getName)
        .anyMatch(newFriendName::equals);
  }

  @Test
  void given_a_not_valid_user_when_a_new_friend_is_added_then_a_parameter_error_is_returned() {
    // given
    var newFriendName = "";
    Long hostId = null;
    final AddFriendParams addFriendParams = new AddFriendParams(newFriendName, hostId);

    // when
    final Either<Validation, User> newFriendResponse = addFriend.execute(addFriendParams);

    // then
    assertThat(newFriendResponse).isNotNull();
    assertThat(newFriendResponse.isLeft()).isTrue();
    assertThat(newFriendResponse.getLeft())
        .isNotNull()
        .extracting(Validation::getErrors)
        .asList()
        .hasSize(2)
        .containsOnlyElementsOf(List.of("The name of the friend cannot be blank", "A new friend needs a valid host"));
  }

  @Test
  void given_an_existing_user_belonging_to_a_group_when_a_new_friend_is_added_then_a_new_friend_added_event_is_published() {
    // given
    var newFriendName = "Paco Gandia";
    var hostId = 3L;
    final AddFriendParams addFriendParams = new AddFriendParams(newFriendName, hostId);

    // when
    addFriend.execute(addFriendParams);

    // then
    assertThat(eventPublisher.getEvents())
        .hasSize(1)
        .hasOnlyElementsOfType(FriendAdded.class);
  }

  @BeforeEach
  void setUp() {
    eventPublisher.clear();
  }
}