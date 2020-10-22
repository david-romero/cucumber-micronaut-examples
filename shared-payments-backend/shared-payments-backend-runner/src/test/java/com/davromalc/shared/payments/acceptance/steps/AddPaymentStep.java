package com.davromalc.shared.payments.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.acceptance.config.World;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.payments.params.AddPaymentParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.List;
import javax.inject.Named;

public class AddPaymentStep {

  private final UseCase<AddPaymentParams, Payment> addPayment;

  private final PaymentRepository paymentRepository;

  private final World world;

  public AddPaymentStep(@Named("addPayment") UseCase<AddPaymentParams, Payment> addPayment, PaymentRepository paymentRepository,
      World world) {
    this.addPayment = addPayment;
    this.paymentRepository = paymentRepository;
    this.world = world;
  }

  @When("the user adds the payment with amount {double} and the description {string}")
  public void theUserAddsThePaymentWithAmountAndTheDescription(double amount, String description) {
    world.setPayment(addPayment.execute(new AddPaymentParams(world.getUserId(), BigDecimal.valueOf(amount), description, null)));
  }

  @Then("a new payment is added to the group")
  public void aNewPaymentIsAddedToTheGroup() {
    final Either<Validation, Payment> payment = world.getPayment();
    assertThat(payment).isNotNull();
    assertThat(payment.isRight()).isTrue();
    assertThat(payment.get()).isNotNull();
    final List<Payment> allPaymentsInTheGroup = paymentRepository.findAllByUserId(List.of(world.getUserId()));
    assertThat(allPaymentsInTheGroup)
        .isNotNull()
        .contains(payment.get());
  }
}
