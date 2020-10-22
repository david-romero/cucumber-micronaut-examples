package com.davromalc.shared.payments.usecase;

import static com.davromalc.shared.payments.domain.shared.Either.right;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import com.davromalc.shared.payments.domain.Balance;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.balance.params.FetchBalanceParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Singleton;

@Singleton
final class FetchBalance implements UseCase<FetchBalanceParams, Balance> {

  public static final Currency EUR = new Currency("€");

  private final GroupRepository groupRepository;

  private final PaymentRepository paymentRepository;

  FetchBalance(GroupRepository groupRepository, PaymentRepository paymentRepository) {
    this.groupRepository = groupRepository;
    this.paymentRepository = paymentRepository;
  }

  @Override
  public Either<Validation, Balance> execute(FetchBalanceParams params) {
    final Validation validation = params.validate();
    if (validation.hasErrors()) {
      return validation.asEither();
    } else {
      final List<User> friends = fetchUsersFromGroup(params);
      final List<Payment> allPayments = fetchPayments(friends);
      final BigDecimal amountByUser = getAmountByFriend(friends, getTotalPaid(allPayments));
      final Map<User, Amount> amountsByUser = friends.stream().collect(toUnmodifiableMap(Function.identity(),
          friend -> new Amount(deriveTheBalance(amountByUser, getTotalPaidByTheFriend(allPayments, friend)), EUR)));
      return right(new Balance(amountsByUser));
    }
  }

  private BigDecimal deriveTheBalance(BigDecimal amountByUser, BigDecimal paidByTheFriend) {
    return amountByUser.multiply(BigDecimal.valueOf(-1)).add(paidByTheFriend);
  }

  private BigDecimal getTotalPaidByTheFriend(List<Payment> allPayments, User friend) {
    return allPayments.stream().filter(payment -> payment.getPayer().equals(friend)).map(Payment::getAmount).map(Amount::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal getAmountByFriend(List<User> friends, BigDecimal totalPaid) {
    return totalPaid.divide(BigDecimal.valueOf(friends.size()), 4, RoundingMode.CEILING);
  }

  private BigDecimal getTotalPaid(List<Payment> allPayments) {
    return allPayments.stream().map(Payment::getAmount).map(Amount::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private List<Payment> fetchPayments(List<User> users) {
    return paymentRepository.findAllByUserId(users.stream().map(User::getId).collect(toUnmodifiableList()));
  }

  private List<User> fetchUsersFromGroup(FetchBalanceParams params) {
    return groupRepository.findGroup(params.getUserId())
        .getFriends().stream()
        .collect(toUnmodifiableList());
  }
}
