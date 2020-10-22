package com.davromalc.shared.payments.infrastructure.persistence;

import static java.util.function.Predicate.not;

import com.davromalc.shared.payments.domain.Group;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import io.micronaut.transaction.annotation.ReadOnly;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
class PostgreSqlGroupRepository implements GroupRepository {

  private final EntityManager entityManager;

  PostgreSqlGroupRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  @ReadOnly
  public Group findGroup(Long userId) {
    return entityManager.createQuery("SELECT g FROM Group g JOIN g.friends friend WHERE friend.id = :id",
        com.davromalc.shared.payments.infrastructure.persistence.entities.Group.class)
        .setParameter("id", userId)
        .getResultStream()
        .findFirst()
        .map(this::mapToDomain)
        .orElse(new Group("", Set.of()));
  }

  @Override
  public Group save(Group group) {
    final com.davromalc.shared.payments.infrastructure.persistence.entities.Group groupEntity = fetchGroupByName(group.getName());
    return findNewUser(group, groupEntity)
        .map(user -> createNewUserEntity(groupEntity, user))
        .map(user -> {
          groupEntity.getFriends().add(user);
          entityManager.persist(user);
          return groupEntity;
        })
        .map(this::mapToDomain)
        .orElse(group);
  }

  private Group mapToDomain(com.davromalc.shared.payments.infrastructure.persistence.entities.Group groupEntity) {
    return new Group(
        groupEntity.getName(),
        groupEntity.getFriends().stream().map(friend -> new User(friend.getId(), friend.getName())).collect(Collectors.toSet()));
  }

  private com.davromalc.shared.payments.infrastructure.persistence.entities.User createNewUserEntity(
      com.davromalc.shared.payments.infrastructure.persistence.entities.Group groupEntity, User user) {
    return new com.davromalc.shared.payments.infrastructure.persistence.entities.User(null, user.getName(), groupEntity);
  }

  private Optional<User> findNewUser(Group group, com.davromalc.shared.payments.infrastructure.persistence.entities.Group groupEntity) {
    return group.getFriends().stream()
        .filter(not(user -> userIsPresent(groupEntity, user)))
        .findAny();
  }

  private boolean userIsPresent(com.davromalc.shared.payments.infrastructure.persistence.entities.Group groupEntity, User user) {
    return groupEntity.getFriends().stream().anyMatch(userEntity -> userEntity.getId().equals(user.getId()));
  }

  private com.davromalc.shared.payments.infrastructure.persistence.entities.Group fetchGroupByName(String groupName) {
    return entityManager
        .createQuery("SELECT g FROM Group g where g.name = :name",
            com.davromalc.shared.payments.infrastructure.persistence.entities.Group.class)
        .setParameter("name", groupName)
        .getSingleResult();
  }
}
