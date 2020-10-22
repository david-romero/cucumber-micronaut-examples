package com.davromalc.shared.payments.domain.payment;

import java.util.List;

public interface PaymentRepository {

  List<Payment> findAllByUserId(List<Long> userIds);

  Payment save(Payment payment);
}
