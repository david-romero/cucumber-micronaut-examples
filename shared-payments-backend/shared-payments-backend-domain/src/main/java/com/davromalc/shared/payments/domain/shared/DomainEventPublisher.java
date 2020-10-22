package com.davromalc.shared.payments.domain.shared;

public interface DomainEventPublisher {

  void publish(DomainEvent event);

}
