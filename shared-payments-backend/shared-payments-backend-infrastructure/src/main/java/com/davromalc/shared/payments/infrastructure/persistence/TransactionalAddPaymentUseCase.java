package com.davromalc.shared.payments.infrastructure.persistence;

import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.infrastructure.persistence.entities.Payment;
import com.davromalc.shared.payments.usecase.payments.params.AddPaymentParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.micronaut.transaction.SynchronousTransactionManager;
import java.sql.Connection;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class TransactionalAddPaymentUseCase implements UseCase<AddPaymentParams, Payment> {

  private final UseCase<AddPaymentParams, Payment> delegatedUseCase;

  private final SynchronousTransactionManager<Connection> transactionManager;

  public TransactionalAddPaymentUseCase(@Named("addPayment") UseCase<AddPaymentParams, Payment> delegatedUseCase,
      SynchronousTransactionManager<Connection> transactionManager) {
    this.delegatedUseCase = delegatedUseCase;
    this.transactionManager = transactionManager;
  }

  @Override
  public Either<Validation, Payment> execute(AddPaymentParams params) {
    return transactionManager.executeWrite(status -> delegatedUseCase.execute(params));
  }
}
