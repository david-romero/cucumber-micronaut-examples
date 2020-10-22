package com.davromalc.shared.payments.infrastructure.persistence.entities;

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
@Table(name = "payment")
@SequenceGenerator(name="payment_sequence", initialValue=300, allocationSize=1)
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sequence")
  private Long id;

  @NotNull
  @Column(name = "description", nullable = false)
  private String description;

  @NotNull
  @Column(name = "amount", nullable = false)
  private long amount;

  @NotNull
  @Column(name = "currency", nullable = false)
  private String currency;

  @NotNull
  @Column(name = "payment_date", nullable = false)
  private long date;

  @ManyToOne(optional = false)
  private User user;

  public Payment() {
  }

  public Payment(Long id, @NotNull String description, @NotNull long amount, @NotNull String currency,
      @NotNull long date, User user) {
    this.id = id;
    this.description = description;
    this.amount = amount;
    this.currency = currency;
    this.date = date;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Payment payment = (Payment) o;
    return Objects.equals(id, payment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Payment{"
        + "id=" + id
        + ", description='" + description + '\''
        + ", amount=" + amount
        + ", currency='" + currency + '\''
        + ", date=" + date
        + ", user=" + user.getId()
        + '}';
  }
}
