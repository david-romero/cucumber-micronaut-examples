package com.davromalc.shared.payments.infrastructure.persistence;

import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.friends.AddFriendParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.micronaut.transaction.SynchronousTransactionManager;
import java.sql.Connection;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class TransactionalAddFriendUseCase implements UseCase<AddFriendParams, User> {

  private final UseCase<AddFriendParams, User> delegatedUseCase;

  private final SynchronousTransactionManager<Connection> transactionManager;

  public TransactionalAddFriendUseCase(@Named("addFriend") UseCase<AddFriendParams, User> delegatedUseCase,
      SynchronousTransactionManager<Connection> transactionManager) {
    this.delegatedUseCase = delegatedUseCase;
    this.transactionManager = transactionManager;
  }

  @Override
  public Either<Validation, User> execute(AddFriendParams params) {
    return transactionManager.executeWrite(status -> delegatedUseCase.execute(params));
  }
}
