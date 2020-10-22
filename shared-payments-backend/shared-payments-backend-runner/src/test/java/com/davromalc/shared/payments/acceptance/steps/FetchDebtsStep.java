package com.davromalc.shared.payments.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.acceptance.config.World;
import com.davromalc.shared.payments.domain.Debt;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.debts.params.FetchDebtsParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Named;

public class FetchDebtsStep {

  private final UseCase<FetchDebtsParams, List<Debt>> fetchDebts;

  private final World world;

  public FetchDebtsStep(@Named("fetchDebts") UseCase<FetchDebtsParams, List<Debt>> fetchDebts, World world) {
    this.fetchDebts = fetchDebts;
    this.world = world;
  }

  @When("the debts are fetched")
  public void theDebtsAreFetched() {
    world.setDebts(fetchDebts.execute(new FetchDebtsParams(world.getUserId())));
  }

  @Then("the debts result is the following")
  public void theDebtsResultIsTheFollowing(DataTable debts) {
    final Either<Validation, List<Debt>> debtsUseCaseOutput = world.getDebts();
    assertThat(debtsUseCaseOutput).isNotNull();
    assertThat(debtsUseCaseOutput.isRight()).isTrue();
    assertThat(debtsUseCaseOutput.get()).isNotNull();
    final List<DebtDto> debtsResult = debtsUseCaseOutput.get().stream().map(DebtDto::asDto).collect(Collectors.toUnmodifiableList());
    final List<DebtDto> debtsAsList =
        debts.asLists().stream().skip(1).map(list -> new DebtDto(list.get(0), list.get(1), new BigDecimal(list.get(2))))
            .collect(Collectors.toUnmodifiableList());
    assertThat(debtsResult).containsExactlyInAnyOrderElementsOf(debtsAsList);
  }

  static class DebtDto {

    private String payer;

    private String recipient;

    private BigDecimal amount;

    public DebtDto(String payer, String recipient, BigDecimal amount) {
      this.payer = payer;
      this.recipient = recipient;
      this.amount = amount;
    }

    public static DebtDto asDto(Debt debt) {
      return new DebtDto(debt.getPayer().getName(), debt.getRecipient().getName(), debt.getAmount().getAmount());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      DebtDto debtDto = (DebtDto) o;
      return Objects.equals(payer, debtDto.payer) &&
          Objects.equals(recipient, debtDto.recipient) &&
          Objects.equals(amount, debtDto.amount);
    }

    @Override
    public int hashCode() {
      return Objects.hash(payer, recipient, amount);
    }

    @Override
    public String toString() {
      return "DebtDto{" +
          "payer='" + payer + '\'' +
          ", recipient='" + recipient + '\'' +
          ", amount=" + amount +
          '}';
    }
  }
}
