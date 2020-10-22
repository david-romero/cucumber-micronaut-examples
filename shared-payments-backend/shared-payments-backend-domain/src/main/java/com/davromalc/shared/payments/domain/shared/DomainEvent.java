package com.davromalc.shared.payments.domain.shared;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class DomainEvent {

  private final Instant happenedAt;

  private final String id;

  private final String entityId;

  public DomainEvent(String id, String entityId, Instant happenedAt) {
    this.id = id;
    this.entityId = entityId;
    this.happenedAt = happenedAt != null ? happenedAt : Instant.now();
  }

  public DomainEvent(String id, Instant happenedAt) {
    this.id = UUID.randomUUID().toString();
    this.entityId = id;
    this.happenedAt = happenedAt != null ? happenedAt : Instant.now();
  }

  public String getId() {
    return id;
  }

  public String getEntityId() {
    return entityId;
  }

  public Instant getHappenedAt() {
    return happenedAt;
  }

  @Override
  public String toString() {
    return "DomainEvent{" + "happenedAt=" + happenedAt + ", id='" + id + '\'' + ", entityId='" + entityId + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DomainEvent that = (DomainEvent) o;
    return Objects.equals(happenedAt, that.happenedAt) && Objects.equals(id, that.id) && Objects.equals(entityId, that.entityId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(happenedAt, id, entityId);
  }
}

