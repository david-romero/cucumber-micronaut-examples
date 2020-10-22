package com.davromalc.shared.payments.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.acceptance.config.World;
import com.davromalc.shared.payments.domain.Balance;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.balance.params.FetchBalanceParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Named;

public class FetchBalanceStep {

  private final UseCase<FetchBalanceParams, Balance> fetchBalance;

  private final World world;

  public FetchBalanceStep(@Named("fetchBalance") UseCase<FetchBalanceParams, Balance> fetchBalance,
      World world) {
    this.fetchBalance = fetchBalance;
    this.world = world;
  }

  @When("the balance is fetched")
  public void theBalanceIsFetched() {
    world.setBalance(fetchBalance.execute(new FetchBalanceParams(world.getUserId())));
  }

  @Then("the balance result is the following")
  public void theBalanceResultIsTheFollowing(DataTable balance) {
    final Either<Validation, Balance> balanceUseCaseOutput = world.getBalance();
    assertThat(balanceUseCaseOutput).isNotNull();
    assertThat(balanceUseCaseOutput.isRight()).isTrue();
    assertThat(balanceUseCaseOutput.get()).isNotNull();
    final Balance balanceResult = balanceUseCaseOutput.get();
    final Map<String, Double> balanceAsMap =
        balance.asLists().stream().skip(1).collect(Collectors.toMap(list -> list.get(0), list -> Double.valueOf(list.get(1))));
    final Map<String, Double> balanceFetched = new HashMap<>();
    balanceResult.getAmount().forEach((user, amount) -> {
      balanceFetched.put(user.getName(), amount.getAmount().doubleValue());
    });
    assertThat(balanceFetched).containsAllEntriesOf(balanceAsMap);
  }
}
