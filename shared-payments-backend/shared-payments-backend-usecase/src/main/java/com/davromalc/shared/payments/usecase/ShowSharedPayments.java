package com.davromalc.shared.payments.usecase;

import static com.davromalc.shared.payments.domain.shared.Either.right;
import static java.util.stream.Collectors.toUnmodifiableList;

import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.payments.params.ShowSharedPaymentsParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import java.util.Comparator;
import java.util.List;
import javax.inject.Singleton;

@Singleton
final class ShowSharedPayments implements UseCase<ShowSharedPaymentsParams, List<Payment>> {

  private final GroupRepository groupRepository;

  private final PaymentRepository paymentRepository;

  public ShowSharedPayments(GroupRepository groupRepository, PaymentRepository paymentRepository) {
    this.groupRepository = groupRepository;
    this.paymentRepository = paymentRepository;
  }

  @Override
  public Either<Validation, List<Payment>> execute(ShowSharedPaymentsParams params) {
    final Validation validation = params.validate();
    if (validation.hasErrors()) {
      return validation.asEither();
    } else {
      final List<Long> friendsIds = fetchUsersFromGroup(params);
      return right(paymentRepository.findAllByUserId(friendsIds).stream()
          .sorted(Comparator.comparing(Payment::getDate).reversed())
          .collect(toUnmodifiableList()));
    }
  }

  private List<Long> fetchUsersFromGroup(ShowSharedPaymentsParams params) {
    return groupRepository.findGroup(params.getUserId())
        .getFriends().stream()
        .map(User::getId)
        .collect(toUnmodifiableList());
  }
}
