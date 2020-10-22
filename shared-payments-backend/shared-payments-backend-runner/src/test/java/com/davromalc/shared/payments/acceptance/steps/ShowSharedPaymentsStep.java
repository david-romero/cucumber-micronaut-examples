package com.davromalc.shared.payments.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.acceptance.config.World;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.payments.params.ShowSharedPaymentsParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import javax.inject.Named;

public class ShowSharedPaymentsStep {

  private final UseCase<ShowSharedPaymentsParams, List<Payment>> showSharedPayments;

  private final World world;

  public ShowSharedPaymentsStep(@Named("showSharedPayments") UseCase<ShowSharedPaymentsParams, List<Payment>> showSharedPayments,
      World world) {
    this.showSharedPayments = showSharedPayments;
    this.world = world;
  }

  @Given("an existing friend group")
  public void anExistingFriendGroup() {
  }

  @Given("an user with identifier {int} belonging to the friend group")
  public void anUserBelongingToTheFriendGroup(int userId) {
    world.setUserId((long) userId);
  }

  @When("the user show the shared payments")
  public void theUserShowTheSharedPayments() {
    world.setPayments(showSharedPayments.execute(new ShowSharedPaymentsParams(world.getUserId())));
  }

  @Then("the payments list contains valid values for the following fields")
  public void thePaymentsListContainsValidValuesForTheFollowingFields(DataTable fields) {
    final Either<Validation, List<Payment>> payments = world.getPayments();
    assertThat(payments.isLeft()).isFalse();
    assertThat(payments).isNotNull();
    assertThat(payments.get()).isNotNull().hasSize(3);
    payments.get().forEach(payment -> assertThat(payment)
        .extracting(fields.asList().toArray(new String[0]))
        .extracting(Object::toString)
        .allMatch(Predicate.not(String::isBlank)));
  }

  @Then("the payments list is sorted by date")
  public void thePaymentsListIsSortedByDate() {
    final Either<Validation, List<Payment>> payments = world.getPayments();
    assertThat(payments).isNotNull();
    assertThat(payments.isLeft()).isFalse();
    assertThat(payments.get()).isSortedAccordingTo(Comparator.comparing(Payment::getDate).reversed());
  }

  @Before
  public void reset() {
    world.reset();
  }
}
