package com.davromalc.shared.payments.infrastructure.persistence.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "group_table") //group is a reserved word in sql
@SequenceGenerator(name="group_sequence", initialValue=300, allocationSize=1)
public class Group implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_sequence")
  private Long id;

  @NotNull
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @OneToMany(mappedBy = "group")
  private Set<User> friends = new HashSet<>();

  public Group() {
  }

  public Group(Long id, @NotNull String name, Set<User> friends) {
    this.id = id;
    this.name = name;
    this.friends = friends;
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

  public Set<User> getFriends() {
    return friends;
  }

  public void setFriends(Set<User> friends) {
    this.friends = friends;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Group group = (Group) o;
    return Objects.equals(name, group.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return "Group{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", friends=" + friends
        + '}';
  }
}
