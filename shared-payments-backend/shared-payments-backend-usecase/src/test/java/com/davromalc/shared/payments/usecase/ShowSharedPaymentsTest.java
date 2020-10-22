package com.davromalc.shared.payments.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.payments.params.ShowSharedPaymentsParams;
import com.davromalc.shared.payments.usecase.shared.ParametersValidation;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import com.davromalc.shared.payments.usecase.stubs.GroupRepositoryStub;
import com.davromalc.shared.payments.usecase.stubs.PaymentRepositoryStub;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ShowSharedPaymentsTest {

  GroupRepository groupRepository = new GroupRepositoryStub();

  PaymentRepository paymentRepository = new PaymentRepositoryStub();

  UseCase<ShowSharedPaymentsParams, List<Payment>> showSharedPayments = new ShowSharedPayments(groupRepository, paymentRepository);

  @Test
  void given_an_user_with_a_friend_group_when_the_payments_are_shown_then_all_fields_are_filled() {
    // given
    final ShowSharedPaymentsParams params = new ShowSharedPaymentsParams(3L);

    // when
    final Either<Validation, List<Payment>> payments = showSharedPayments.execute(params);

    // then
    assertThat(payments.isLeft()).isFalse();
    payments.get().forEach(payment -> {
      assertThat(payment).isNotNull();
      assertThat(payment).extracting(Payment::getAmount).isNotNull();
      assertThat(payment).extracting(Payment::getDate).isNotNull();
      assertThat(payment).extracting(Payment::getDescription).asString().isNotBlank();
      assertThat(payment).extracting(Payment::getPayer).isNotNull();
    });
  }

  @Test
  void given_an_user_with_a_friend_group_when_the_payments_are_shown_then_all_payments_are_sorted_descending() {
    // given
    final ShowSharedPaymentsParams params = new ShowSharedPaymentsParams(3L);

    // when
    final Either<Validation, List<Payment>> payments = showSharedPayments.execute(params);

    // then
    assertThat(payments.isLeft()).isFalse();
    final List<Payment> paymentList = payments.get();
    assertThat(paymentList)
        .isSortedAccordingTo(Comparator.comparing(Payment::getDate).reversed());
  }

  @Test
  void given_a_not_valid_user_when_the_payments_are_shown_then_a_validation_error_is_returned() {
    // given
    final ShowSharedPaymentsParams params = new ShowSharedPaymentsParams(null);

    // when
    final Either<Validation, List<Payment>> payments = showSharedPayments.execute(params);

    // then
    assertThat(payments.isLeft()).isTrue();
    final Validation validation = payments.getLeft();
    assertThat(validation)
        .isEqualTo(new ParametersValidation(List.of("The user identifier cannot be null")));
  }
}