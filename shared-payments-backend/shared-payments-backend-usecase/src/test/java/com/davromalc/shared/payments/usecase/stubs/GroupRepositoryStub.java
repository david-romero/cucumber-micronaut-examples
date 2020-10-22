package com.davromalc.shared.payments.usecase.stubs;

import com.davromalc.shared.payments.domain.Group;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GroupRepositoryStub implements GroupRepository {

  private Map<Long, Set<User>> database = Map.of(
      1L, Set.of(new User(1, "User 1"), new User(2, "User 2"), new User(3, "User 3")),
      2L, Set.of(new User(4, "User 4"), new User(5, "User 5")),
      3L, Set.of(new User(6, "User 6"), new User(7, "User 7")),
      4L, Set.of(new User(8, "User 8"), new User(9, "User 9"), new User(10, "User 10"), new User(11, "User 11")),
      5L, Set.of(new User(12, "Francisco Buyo"), new User(13, "Alfonso Perez"), new User(14, "Raul Gonzalez"), new User(15, "Jose María Gutierrez"))
      );

  @Override
  public Group findGroup(Long userId) {
    return database.entrySet()
        .stream()
        .filter(tuple -> tuple.getValue().contains(new User(userId, "")))
        .findFirst()
        .map(tuple -> new Group(tuple.getKey().toString(), tuple.getValue()))
        .orElse(new Group("0", Set.of()));
  }

  @Override
  public Group save(Group group) {
    final Long groupId = database.entrySet().stream()
        .filter(tuple -> {
          final HashSet<User> users = new HashSet<>(tuple.getValue());
          users.retainAll(group.getFriends());
          return !users.isEmpty();
        })
        .findAny()
        .map(Entry::getKey)
        .orElse(0L);

    final HashMap<Long, Set<User>> database = new HashMap<>(this.database);
    database.put(groupId, group.getFriends());
    this.database = Map.copyOf(database);
    return group;
  }
}
