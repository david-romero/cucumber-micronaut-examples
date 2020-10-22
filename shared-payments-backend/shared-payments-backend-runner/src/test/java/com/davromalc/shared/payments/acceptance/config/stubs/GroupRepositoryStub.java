package com.davromalc.shared.payments.acceptance.config.stubs;

import com.davromalc.shared.payments.domain.Group;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.inject.Singleton;

@Requires(env = "acceptance")
@Singleton
@Primary
public class GroupRepositoryStub implements GroupRepository {

  private Map<Long, Set<User>> database =
      Map.of(
          1L, Set.of(new User(1, "Julio Baptista"), new User(2, "David Romero"), new User(3, "Juan Rios")),
          2L, Set.of(new User(4, "Francisco Buyo"), new User(5, "Alfonso Perez"), new User(6, "Raul Gonzalez"), new User(7, "Jose Maria Gutierrez"))
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
