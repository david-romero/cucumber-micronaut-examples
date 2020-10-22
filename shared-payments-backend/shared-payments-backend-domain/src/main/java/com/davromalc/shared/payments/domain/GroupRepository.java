package com.davromalc.shared.payments.domain;

public interface GroupRepository {

  Group findGroup(Long userId);

  Group save(Group group);
}
