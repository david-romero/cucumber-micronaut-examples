package com.davromalc.shared.payments.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.domain.Debt;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.debts.params.FetchDebtsParams;
import com.davromalc.shared.payments.usecase.shared.ParametersValidation;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import com.davromalc.shared.payments.usecase.stubs.GroupRepositoryStub;
import com.davromalc.shared.payments.usecase.stubs.PaymentRepositoryStub;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FetchDebtsTest {

  GroupRepository groupRepository = new GroupRepositoryStub();

  PaymentRepository paymentRepository = new PaymentRepositoryStub();

  UseCase<FetchDebtsParams, List<Debt>> fetchDebts = new FetchDebts(paymentRepository, groupRepository);

  @Test
  void given_a_friends_group_with_several_payments_when_the_debts_are_fetched_then_two_debts_are_returned() {
    // given
    var params = new FetchDebtsParams(3L);

    // when
    final Either<Validation, List<Debt>> debtsResult = fetchDebts.execute(params);

    // then
    assertThat(debtsResult.isRight()).isTrue();
    assertThat(debtsResult.get()).isNotNull();
    assertThat(debtsResult.get())
        .containsOnlyElementsOf(List.of(
            new Debt(new User(1L, "User 1"), new User(3L, "User 3"), new Amount(BigDecimal.valueOf(25.3334), new Currency("€"))),
            new Debt(new User(2L, "User 2"), new User(3L, "User 3"), new Amount(BigDecimal.valueOf(15.3332), new Currency("€")))
        ));
  }

  @Test
  void given_two_member_group_with_several_payments_when_the_debts_are_fetched_then_one_debt_is_returned() {
    // given
    var params = new FetchDebtsParams(4L);

    // when
    final Either<Validation, List<Debt>> debtsResult = fetchDebts.execute(params);

    // then
    assertThat(debtsResult.isRight()).isTrue();
    assertThat(debtsResult.get()).isNotNull();
    assertThat(debtsResult.get())
        .containsOnlyElementsOf(List.of(
            new Debt(new User(5L, "User 5"), new User(4L, "User 4"), new Amount(new BigDecimal("177.4350"), new Currency("€")))
        ));
  }

  @Test
  void given_two_member_group_with_no_payments_when_the_debts_are_fetched_then_no_debts_are_returned() {
    // given
    var params = new FetchDebtsParams(6L);

    // when
    final Either<Validation, List<Debt>> debtsResult = fetchDebts.execute(params);

    // then
    assertThat(debtsResult.isRight()).isTrue();
    assertThat(debtsResult.get()).isNotNull();
    assertThat(debtsResult.get()).isEmpty();
  }

  @Test
  void given_a_friends_group_with_several_payments_when_the_debts_are_fetched_then_four_debts_are_returned() {
    // given
    var params = new FetchDebtsParams(9L);

    // when
    final Either<Validation, List<Debt>> debtsResult = fetchDebts.execute(params);

    // then
    assertThat(debtsResult.isRight()).isTrue();
    assertThat(debtsResult.get()).isNotNull();
    assertThat(debtsResult.get())
        .containsOnlyElementsOf(List.of(
            new Debt(new User(8L, "User 8"), new User(10L, "User 10"), new Amount(BigDecimal.valueOf(24.7875), new Currency("€"))),
            new Debt(new User(9L, "User 9"), new User(10L, "User 10"), new Amount(new BigDecimal("8.4250"), new Currency("€"))),
            new Debt(new User(9L, "User 9"), new User(11L, "User 11"), new Amount(new BigDecimal("0.2125"), new Currency("€")))
        ));
  }

  @Test
  void given_a_not_valid_user_when_the_debts_are_fetched_then_a_validation_error_is_returned() {
    // given
    var params = new FetchDebtsParams(null);

    // when
    final Either<Validation, List<Debt>> debtsResult = fetchDebts.execute(params);

    // then
    assertThat(debtsResult.isLeft()).isTrue();
    final Validation validation = debtsResult.getLeft();
    assertThat(validation)
        .isEqualTo(new ParametersValidation(List.of("The user identifier cannot be null")));
  }

  @Test
  void given_four_member_group_with_several_payments_when_the_debts_are_fetched_then_four_debts_are_returned() {
    // given
    var params = new FetchDebtsParams(12L);

    // when
    final Either<Validation, List<Debt>> debtsResult = fetchDebts.execute(params);

    // then
    assertThat(debtsResult.isRight()).isTrue();
    assertThat(debtsResult.get()).isNotNull();
    assertThat(debtsResult.get())
        .containsOnlyElementsOf(List.of(
            new Debt(new User(14L, "Raul Gonzalez"), new User(12L, "Francisco Buyo"), new Amount(new BigDecimal("40.8500"), new Currency("€"))),
            new Debt(new User(15L, "Jose Maria Gutierrez"), new User(12L, "Francisco Buyo"), new Amount(new BigDecimal("18.3000"), new Currency("€"))),
            new Debt(new User(15L, "Jose Maria Gutierrez"), new User(13L, "Alfonso Perez"), new Amount(new BigDecimal("22.5500"), new Currency("€"))))
        );
  }
}