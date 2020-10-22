package com.davromalc.shared.payments.infrastructure.persistence;

import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import io.micronaut.transaction.annotation.ReadOnly;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
class PostgreSqlPaymentRepository implements PaymentRepository {

  private final EntityManager entityManager;

  PostgreSqlPaymentRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  @ReadOnly
  public List<Payment> findAllByUserId(List<Long> userIds) {
    return entityManager.createQuery("SELECT p FROM Payment p where p.user.id in :ids",
        com.davromalc.shared.payments.infrastructure.persistence.entities.Payment.class)
        .setParameter("ids", userIds)
        .getResultStream()
        .map(payment -> mapToDomain(payment))
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public Payment save(Payment payment) {
    final com.davromalc.shared.payments.infrastructure.persistence.entities.User user =
        entityManager.find(com.davromalc.shared.payments.infrastructure.persistence.entities.User.class, payment.getPayer().getId());
    final com.davromalc.shared.payments.infrastructure.persistence.entities.Payment paymentEntity =
        new com.davromalc.shared.payments.infrastructure.persistence.entities.Payment(
            null,
            payment.getDescription(),
            payment.getAmount().getAmount().movePointRight(4).longValueExact(),
            payment.getAmount().getCurrency().getSymbol(),
            payment.getDate().toInstant().toEpochMilli(),
            user);
    entityManager.persist(paymentEntity);
    return mapToDomain(paymentEntity).withPayer(new User(user.getId(), user.getName()));
  }

  private Payment mapToDomain(com.davromalc.shared.payments.infrastructure.persistence.entities.Payment payment) {
    return new Payment(
        new User(payment.getUser().getId(), payment.getUser().getName()),
        new Amount(BigDecimal.valueOf(payment.getAmount()).movePointLeft(4), new Currency(payment.getCurrency())),
        payment.getDescription(),
        Instant.ofEpochMilli(payment.getDate()).atOffset(ZoneOffset.UTC));
  }
}
