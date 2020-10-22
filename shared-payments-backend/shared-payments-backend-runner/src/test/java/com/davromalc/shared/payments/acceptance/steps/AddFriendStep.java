package com.davromalc.shared.payments.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.acceptance.config.World;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.usecase.friends.AddFriendParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Optional;
import javax.inject.Named;

public class AddFriendStep {

  private final UseCase<AddFriendParams, User> addFriend;

  private final GroupRepository groupRepository;

  private final World world;

  public AddFriendStep(@Named("addFriend") UseCase<AddFriendParams, User> addFriend, GroupRepository groupRepository,
      World world) {
    this.addFriend = addFriend;
    this.groupRepository = groupRepository;
    this.world = world;
  }

  @When("the user adds {string} to the group")
  public void theUserAddsFriendToTheGroup(String friendName) {
    addFriend.execute(new AddFriendParams(friendName, world.getUserId()));
  }

  @Then("the new friend {string} belongs to the same group than the user with identifier {int}")
  public void theNewFriendBelongsToTheSameGroupThanTheUserWithIdentifier(String newFriendName, int userId) {
    final Optional<String> newFriendNameFromDatabase = groupRepository.findGroup((long) userId)
        .getFriends().stream()
        .map(User::getName)
        .filter(newFriendName::equals)
        .findAny();
    assertThat(newFriendNameFromDatabase)
        .matches(Optional::isPresent)
        .get()
        .asString()
        .isEqualTo(newFriendName);
  }
}
