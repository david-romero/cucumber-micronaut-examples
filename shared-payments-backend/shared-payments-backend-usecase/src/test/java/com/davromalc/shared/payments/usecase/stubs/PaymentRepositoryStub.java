package com.davromalc.shared.payments.usecase.stubs;

import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentRepositoryStub implements PaymentRepository {

  private Map<Long, List<Payment>> payments = new HashMap<>();

  public PaymentRepositoryStub() {
    this.payments.putAll(Map.of(
        1L, List.of(),
        2L, List.of(new Payment(new User(2, "David Romero"), new Amount(BigDecimal.TEN, new Currency("€")), "Burger King",
            OffsetDateTime.parse("2020-09-16T19:59:00+02:00"))),
        3L, List.of(
            new Payment(new User(3, "Juan Rios"), new Amount(BigDecimal.valueOf(54), new Currency("€")), "SuperMarket",
                OffsetDateTime.parse("2020-09-16T21:00:00+02:00")),
            new Payment(new User(3, "Juan Rios"), new Amount(BigDecimal.valueOf(12), new Currency("€")), "Car Renting",
                OffsetDateTime.parse("2020-09-16T21:00:05+02:00"))),
        5L, List.of(),
        4L, List.of(new Payment(new User(4, "User 4"), new Amount(BigDecimal.valueOf(354.87), new Currency("€")), "Burger King",
            OffsetDateTime.parse("2020-09-16T19:59:00+02:00"))),
        6L, List.of(),
        7L, List.of(),
        8L, List.of(
            new Payment(new User(8, "User 8"), new Amount(BigDecimal.valueOf(8), new Currency("€")), "Burger King",
                OffsetDateTime.parse("2020-09-16T19:59:00+02:00"))),
        9L, List.of(
            new Payment(new User(9, "User 9"), new Amount(BigDecimal.valueOf(22), new Currency("€")), "SuperMarket",
                OffsetDateTime.parse("2020-09-16T21:00:00+02:00")),
            new Payment(new User(9, "User 9"), new Amount(BigDecimal.valueOf(2.15), new Currency("€")), "Car Renting",
                OffsetDateTime.parse("2020-09-16T21:00:05+02:00"))),
        10L, List.of(
            new Payment(new User(10, "User 10"), new Amount(BigDecimal.valueOf(54), new Currency("€")), "SuperMarket",
                OffsetDateTime.parse("2020-09-16T21:00:00+02:00")),
            new Payment(new User(10, "User 10"), new Amount(BigDecimal.valueOf(12), new Currency("€")), "Car Renting",
                OffsetDateTime.parse("2020-09-16T21:00:05+02:00"))
        )
    ));
    this.payments.put(11L, List.of(
        new Payment(new User(11, "User 11"), new Amount(BigDecimal.valueOf(33), new Currency("€")), "Burger King",
            OffsetDateTime.parse("2020-09-16T19:59:00+02:00"))));
    this.payments.put(12L, List.of(
        new Payment(new User(12, "Francisco Buyo"), new Amount(BigDecimal.valueOf(100), new Currency("€")), "Cena",
            OffsetDateTime.parse("2020-09-21T21:00:00+02:00"))));
    this.payments.put(13L, List.of(
        new Payment(new User(13, "Alfonso Perez"), new Amount(BigDecimal.TEN, new Currency("€")), "Taxi",
            OffsetDateTime.parse("2020-09-20T18:00:00+02:00")),
        new Payment(new User(13, "Alfonso Perez"), new Amount(BigDecimal.valueOf(53.40), new Currency("€")), "Compra",
            OffsetDateTime.parse("2020-09-20T15:00:05+02:00"))));
  }

  @Override
  public List<Payment> findAllByUserId(List<Long> userIds) {
    return userIds.stream()
        .map(userId -> payments.getOrDefault(userId, List.of()))
        .flatMap(List::stream)
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public Payment save(Payment payment) {
    final Map<Long, List<Payment>> payments = new HashMap<>(this.payments);
    if (payments.containsKey(payment.getPayer().getId())) {
      final ArrayList<Payment> newPayments = new ArrayList<>(payments.get(payment.getPayer().getId()));
      newPayments.add(payment);
      payments.put(payment.getPayer().getId(), newPayments);
    } else {
      payments.put(payment.getPayer().getId(), List.of(payment));
    }
    this.payments = payments;
    return payment;
  }
}
