package com.davromalc.shared.payments.usecase;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.toUnmodifiableList;

import com.davromalc.shared.payments.domain.Debt;
import com.davromalc.shared.payments.domain.GroupRepository;
import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.domain.payment.Amount;
import com.davromalc.shared.payments.domain.payment.Currency;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.domain.payment.PaymentRepository;
import com.davromalc.shared.payments.domain.shared.Either;
import com.davromalc.shared.payments.domain.shared.Validation;
import com.davromalc.shared.payments.usecase.debts.params.FetchDebtsParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.inject.Singleton;

@Singleton
final class FetchDebts implements UseCase<FetchDebtsParams, List<Debt>> {

  public static final Currency EURO = new Currency("€");

  private final PaymentRepository paymentRepository;

  private final GroupRepository groupRepository;

  public FetchDebts(PaymentRepository paymentRepository, GroupRepository groupRepository) {
    this.paymentRepository = paymentRepository;
    this.groupRepository = groupRepository;
  }

  @Override
  public Either<Validation, List<Debt>> execute(FetchDebtsParams params) {
    final Validation validation = params.validate();
    if (validation.hasErrors()) {
      return validation.asEither();
    } else {
      final List<Debt> debts = new ArrayList<>();
      final List<User> friends = fetchUsersFromGroup(params);
      final List<Payment> allPayments = fetchPayments(friends);
      final BigDecimal amountByFriend = getAmountByFriend(friends, getTotalPaid(allPayments));
      Map<User, BigDecimal> paidByUser = getPaidByUser(friends, allPayments);
      for (final User friend : getFriendsWithDebts(friends, allPayments, amountByFriend)) {
        final BigDecimal debtAmount = amountByFriend.subtract(getTotalPaidByTheFriend(allPayments, friend));
        boolean exitsDebts = isGreaterThanZero(debtAmount);
        while (exitsDebts) {
          final Entry<User, BigDecimal> friendWhoHasPaidTheMost = getLastElement(paidByUser);
          final Debt debt = new Debt(friend, friendWhoHasPaidTheMost.getKey(),
              new Amount(getDebtAmount(amountByFriend, debtAmount, friendWhoHasPaidTheMost), EURO));
          paidByUser = updatePaidByUser(paidByUser, debt);
          debts.add(debt);
          exitsDebts = isNotTheSameAmount(paidByUser.get(friend), amountByFriend);
        }
      }
      return Either.right(debts);
    }
  }

  private boolean isGreaterThanZero(BigDecimal debtAmount) {
    return debtAmount.compareTo(ZERO) > 0;
  }

  private boolean isNotTheSameAmount(BigDecimal paidByTheFriend, BigDecimal amountByFriend) {
    return paidByTheFriend.setScale(3, RoundingMode.CEILING).subtract(amountByFriend.setScale(3, RoundingMode.CEILING))
        .setScale(2, RoundingMode.CEILING).compareTo(BigDecimal.ZERO) != 0;
  }

  private Map<User, BigDecimal> updatePaidByUser(Map<User, BigDecimal> paidByUser, Debt debt) {
    paidByUser.compute(debt.getRecipient(), (user, amount) -> amount.subtract(debt.getAmount().getAmount()));
    paidByUser.compute(debt.getPayer(), (user, amount) -> amount.add(debt.getAmount().getAmount()));
    return sortByPaid(paidByUser);
  }

  private BigDecimal getDebtAmount(BigDecimal amountByFriend, BigDecimal debtAmount, Entry<User, BigDecimal> mostPaid) {
    if (mostPaid.getValue().subtract(debtAmount).compareTo(amountByFriend) < 0) {
      return mostPaid.getValue().subtract(amountByFriend);
    } else {
      return debtAmount;
    }
  }

  private BigDecimal getTotalPaid(List<Payment> allPayments) {
    return allPayments.stream().map(Payment::getAmount).map(Amount::getAmount).reduce(ZERO, BigDecimal::add);
  }

  private List<User> getFriendsWithDebts(List<User> friends, List<Payment> allPayments, BigDecimal amountByFriend) {
    return friends.stream().filter(user -> {
      final BigDecimal totalPaidByTheFriend = getTotalPaidByTheFriend(allPayments, user);
      return totalPaidByTheFriend.compareTo(amountByFriend) < 0;
    }).sorted(Comparator.comparing(User::getId))
        .collect(toUnmodifiableList());

  }

  private BigDecimal getTotalPaidByTheFriend(List<Payment> allPayments, User friend) {
    return allPayments.stream()
        .filter(payment -> payment.getPayer().equals(friend))
        .map(Payment::getAmount)
        .map(Amount::getAmount)
        .reduce(ZERO, BigDecimal::add);
  }

  private Map<User, BigDecimal> getPaidByUser(List<User> friends, List<Payment> allPayments) {
    Map<User, BigDecimal> paidByUser = new HashMap<>();
    for (User friend : friends) {
      paidByUser.put(friend, getTotalPaidByTheFriend(allPayments, friend));
    }
    return sortByPaid(paidByUser);
  }

  private Map<User, BigDecimal> sortByPaid(Map<User, BigDecimal> paidByUser) {
    return paidByUser.entrySet().stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  private BigDecimal getAmountByFriend(List<User> friends, BigDecimal totalPaid) {
    return totalPaid.divide(BigDecimal.valueOf(friends.size()), 4, RoundingMode.CEILING);
  }

  private List<Payment> fetchPayments(List<User> users) {
    return paymentRepository.findAllByUserId(users.stream().map(User::getId).collect(toUnmodifiableList()));
  }

  private List<User> fetchUsersFromGroup(FetchDebtsParams params) {
    return groupRepository.findGroup(params.getUserId())
        .getFriends().stream()
        .collect(toUnmodifiableList());
  }

  private Entry<User, BigDecimal> getLastElement(Map<User, BigDecimal> map) {
    Entry<User, BigDecimal> lastElement = null;
    final Iterator<Entry<User, BigDecimal>> iterator = map.entrySet().iterator();
    while (iterator.hasNext()) {
      lastElement = iterator.next();
    }
    return lastElement != null ? lastElement : map.entrySet().iterator().next();
  }
}
