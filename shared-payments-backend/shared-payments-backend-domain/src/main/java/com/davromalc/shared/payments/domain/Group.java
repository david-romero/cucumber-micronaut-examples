package com.davromalc.shared.payments.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Group {

  private final String name;

  private final Set<User> friends;

  public Group(String name, Set<User> friends) {
    this.name = name;
    this.friends = Collections.unmodifiableSet(friends);
  }

  public Set<User> getFriends() {
    return Collections.unmodifiableSet(friends);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Group group = (Group) o;
    return Objects.equals(friends, group.friends) && Objects.equals(name, group.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(friends, name);
  }

  @Override
  public String toString() {
    return "Group{"
        + "friends=" + friends
        + "name=" + name
        + '}';
  }

  public Group withNewFriend(User newFriend) {
    final Set<User> friends = new HashSet<>(this.friends);
    friends.add(newFriend);
    return new Group(name, Set.copyOf(friends));
  }
}
