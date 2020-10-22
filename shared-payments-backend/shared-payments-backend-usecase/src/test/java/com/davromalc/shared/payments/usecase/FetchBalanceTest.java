package com.davromalc.shared.payments.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.domain.Balance;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.balance.params.FetchBalanceParams;
import com.davromalc.shared.payments.usecase.shared.ParametersValidation;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import com.davromalc.shared.payments.usecase.stubs.GroupRepositoryStub;
import com.davromalc.shared.payments.usecase.stubs.PaymentRepositoryStub;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FetchBalanceTest {

  GroupRepository groupRepository = new GroupRepositoryStub();

  PaymentRepository paymentRepository = new PaymentRepositoryStub();

  UseCase<FetchBalanceParams, Balance> fetchBalance = new FetchBalance(groupRepository, paymentRepository);

  @Test
  void given_a_two_friends_group_with_1_payment_by_the_first_friend_when_the_balance_is_fetched_then_the_balance_is_for_the_second_friend() {
    // given
    var params = new FetchBalanceParams(4L);

    // when
    final Either<Validation, Balance> balanceResult = fetchBalance.execute(params);

    // then
    assertThat(balanceResult.isRight()).isTrue();
    assertThat(balanceResult.get()).isNotNull();
    assertThat(balanceResult.get().getAmount())
        .containsEntry(new User(4, "User 4"), new Amount(new BigDecimal("177.4350"), new Currency("€")))
        .containsEntry(new User(5, "User 5"), new Amount(new BigDecimal("-177.4350"), new Currency("€")));
  }

  @Test
  void given_a_two_friends_group_with_no_payments_when_the_balance_is_fetched_then_the_balance_is_zero_for_all_members() {
    // given
    var params = new FetchBalanceParams(6L);

    // when
    final Either<Validation, Balance> balanceResult = fetchBalance.execute(params);

    // then
    assertThat(balanceResult.isRight()).isTrue();
    assertThat(balanceResult.get()).isNotNull();
    assertThat(balanceResult.get().getAmount())
        .containsEntry(new User(6, "User 6"), new Amount(BigDecimal.ZERO.setScale(4, RoundingMode.CEILING), new Currency("€")))
        .containsEntry(new User(7, "User 7"), new Amount(BigDecimal.ZERO.setScale(4, RoundingMode.CEILING), new Currency("€")));
  }

  @Test
  void given_a_friends_group_with_several_payments_when_the_balance_is_fetched_then_the_balance_is_returned() {
    // given
    var params = new FetchBalanceParams(3L);

    // when
    final Either<Validation, Balance> balanceResult = fetchBalance.execute(params);

    // then
    assertThat(balanceResult.isRight()).isTrue();
    assertThat(balanceResult.get()).isNotNull();
    assertThat(balanceResult.get().getAmount())
        .containsEntry(new User(1, "User 1"), new Amount(BigDecimal.valueOf(-25.3334), new Currency("€")))
        .containsEntry(new User(2, "User 2"), new Amount(BigDecimal.valueOf(-15.3334), new Currency("€")))
        .containsEntry(new User(3, "User 3"), new Amount(BigDecimal.valueOf(40.6666), new Currency("€")));
  }

  @Test
  void given_a_not_valid_user_when_the_balance_is_fetched_then_a_validation_error_is_returned() {
    // given
    var params = new FetchBalanceParams(null);

    // when
    final Either<Validation, Balance> payments = fetchBalance.execute(params);

    // then
    assertThat(payments.isLeft()).isTrue();
    final Validation validation = payments.getLeft();
    assertThat(validation)
        .isEqualTo(new ParametersValidation(List.of("The user identifier cannot be null")));
  }
}