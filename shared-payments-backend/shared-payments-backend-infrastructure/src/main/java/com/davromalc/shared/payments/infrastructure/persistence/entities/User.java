package com.davromalc.shared.payments.infrastructure.persistence.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_table") //user is a reserved word
@SequenceGenerator(name="user_sequence", initialValue=500, allocationSize=1)
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne
  private Group group;

  public User() {
  }

  public User(Long id, @NotNull String name, Group group) {
    this.id = id;
    this.name = name;
    this.group = group;
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

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "User{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", group=" + group.getName()
        + '}';
  }
}
