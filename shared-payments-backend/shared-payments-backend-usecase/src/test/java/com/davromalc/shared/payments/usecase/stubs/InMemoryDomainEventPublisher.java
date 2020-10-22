package com.davromalc.shared.payments.usecase.stubs;

import com.davromalc.shared.payments.domain.shared.DomainEvent;
import com.davromalc.shared.payments.domain.shared.DomainEventPublisher;
import java.util.ArrayList;
import java.util.List;

public class InMemoryDomainEventPublisher implements DomainEventPublisher {

  private final List<DomainEvent> events = new ArrayList<>();

  @Override
  public void publish(DomainEvent event) {
    events.add(event);
  }

  public List<DomainEvent> getEvents() {
    return events;
  }

  public void clear() {
    events.clear();
  }
}
