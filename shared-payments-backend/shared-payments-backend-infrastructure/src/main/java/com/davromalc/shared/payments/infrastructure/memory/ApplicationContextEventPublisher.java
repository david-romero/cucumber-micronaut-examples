package com.davromalc.shared.payments.infrastructure.memory;

import com.davromalc.shared.payments.domain.shared.DomainEvent;
import com.davromalc.shared.payments.domain.shared.DomainEventPublisher;
import io.micronaut.context.event.ApplicationEventPublisher;
import javax.inject.Singleton;

@Singleton
class ApplicationContextEventPublisher implements DomainEventPublisher {

  private final ApplicationEventPublisher delegatedEventPublisher;

  ApplicationContextEventPublisher(ApplicationEventPublisher delegatedEventPublisher) {
    this.delegatedEventPublisher = delegatedEventPublisher;
  }

  @Override
  public void publish(DomainEvent event) {
    delegatedEventPublisher.publishEvent(event);
  }
}
