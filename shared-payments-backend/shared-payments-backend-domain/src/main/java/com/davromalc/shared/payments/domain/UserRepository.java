package com.davromalc.shared.payments.domain;

import java.util.Optional;

public interface UserRepository {

  Optional<User> findById(Long id);

}
