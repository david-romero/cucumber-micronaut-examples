package com.davromalc.shared.payments.infrastructure.rest.dto;

import java.io.Serializable;
import java.util.Objects;

public class UserDto implements Serializable {

  private Long id;

  private String name;

  public UserDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public UserDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    UserDto userDto = (UserDto) o;
    return Objects.equals(id, userDto.id) &&
        Objects.equals(name, userDto.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "UserDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
