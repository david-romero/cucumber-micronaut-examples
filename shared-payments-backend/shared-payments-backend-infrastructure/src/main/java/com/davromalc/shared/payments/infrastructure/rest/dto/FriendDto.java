package com.davromalc.shared.payments.infrastructure.rest.dto;

import java.io.Serializable;
import java.util.Objects;

public class FriendDto implements Serializable {

  private String name;

  public FriendDto(String name) {
    this.name = name;
  }

  public FriendDto() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FriendDto friendDto = (FriendDto) o;
    return Objects.equals(name, friendDto.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return "FriendDto{" +
        "name='" + name + '\'' +
        '}';
  }
}
