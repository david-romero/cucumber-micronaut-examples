package com.davromalc.shared.payments.infrastructure.rest;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import com.davromalc.shared.payments.domain.Balance;
import com.davromalc.shared.payments.domain.Debt;
import com.davromalc.shared.payments.domain.payment.Payment;
import com.davromalc.shared.payments.infrastructure.rest.dto.BalanceDto;
import com.davromalc.shared.payments.infrastructure.rest.dto.DebtDto;
import com.davromalc.shared.payments.infrastructure.rest.dto.PaymentCreationDto;
import com.davromalc.shared.payments.infrastructure.rest.dto.PaymentDto;
import com.davromalc.shared.payments.infrastructure.rest.dto.UserDto;
import com.davromalc.shared.payments.usecase.balance.params.FetchBalanceParams;
import com.davromalc.shared.payments.usecase.debts.params.FetchDebtsParams;
import com.davromalc.shared.payments.usecase.payments.params.AddPaymentParams;
import com.davromalc.shared.payments.usecase.payments.params.ShowSharedPaymentsParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpResponseFactory;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.simple.SimpleHttpResponseFactory;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Controller("/api/payment/")
class PaymentController {

  private static final DecimalFormat EURO_FORMAT = new DecimalFormat("#,###.00 €");

  private final UseCase<ShowSharedPaymentsParams, List<Payment>> showSharedPayments;

  private final UseCase<AddPaymentParams, Payment> addPayment;

  private final UseCase<FetchBalanceParams, Balance> fetchBalance;

  private final UseCase<FetchDebtsParams, List<Debt>> fetchDebts;

  private final HttpResponseFactory httpResponseFactory = SimpleHttpResponseFactory.INSTANCE;

  PaymentController(@Named("showSharedPayments") UseCase<ShowSharedPaymentsParams, List<Payment>> showSharedPayments,
      @Named("transactionalAddPaymentUseCase") UseCase<AddPaymentParams, Payment> addPayment,
      @Named("fetchBalance") UseCase<FetchBalanceParams, Balance> fetchBalance,
      @Named("fetchDebts") UseCase<FetchDebtsParams, List<Debt>> fetchDebts) {
    this.showSharedPayments = showSharedPayments;
    this.addPayment = addPayment;
    this.fetchBalance = fetchBalance;
    this.fetchDebts = fetchDebts;
  }

  @Get(uri = "group/user/{userId}", processes = MediaType.APPLICATION_JSON)
  List<PaymentDto> showSharedPayments(@PathVariable("userId") Integer userId) {
    return showSharedPayments.execute(new ShowSharedPaymentsParams(Long.valueOf(userId)))
        .map(payments -> payments.stream()
            .map(this::mapToDto)
            .collect(Collectors.toUnmodifiableList()))
        .getOrElse(List.of());
  }

  @Post(uri = "user/{userId}", processes = MediaType.APPLICATION_JSON)
  HttpResponse<?> addPayment(@PathVariable("userId") Integer userId, @NotNull @Body PaymentCreationDto paymentDto) {
    return addPayment.execute(
        new AddPaymentParams(Long.valueOf(userId), BigDecimal.valueOf(paymentDto.getAmount()), paymentDto.getDescription(),
            paymentDto.getDate()))
        .map(payment -> new PaymentDto(payment.getDescription(), formatAmount(payment.getAmount().getAmount()),
            payment.getDate().format(ISO_DATE_TIME), payment.getPayer().getName()))
        .map(user -> httpResponseFactory.status(HttpStatus.CREATED, user))
        .fold(HttpResponse::badRequest, Function.identity());
  }

  @Get(uri = "user/{userId}/balance", processes = MediaType.APPLICATION_JSON)
  HttpResponse<?> fetchBalance(@PathVariable("userId") Integer userId) {
    return fetchBalance.execute(new FetchBalanceParams(Long.valueOf(userId)))
        .map(balance -> new BalanceDto(balance.getAmount().entrySet().stream().collect(Collectors.toMap(
            tuple -> tuple.getKey().getName(),
            tuple -> formatAmount(tuple.getValue().getAmount())))))
        .map(balance -> httpResponseFactory.status(HttpStatus.OK, balance))
        .fold(HttpResponse::badRequest, Function.identity());
  }

  @Get(uri = "user/{userId}/debts", processes = MediaType.APPLICATION_JSON)
  HttpResponse<?> fetchDebts(@PathVariable("userId") Integer userId) {
    return fetchDebts.execute(new FetchDebtsParams(Long.valueOf(userId)))
        .map(debts -> debts.stream().map(this::toDto).collect(Collectors.toUnmodifiableList()))
        .map(balance -> httpResponseFactory.status(HttpStatus.OK, balance))
        .fold(HttpResponse::badRequest, Function.identity());
  }

  private DebtDto toDto(Debt debt) {
    return new DebtDto(
        new UserDto(debt.getPayer().getId(), debt.getPayer().getName()),
        new UserDto(debt.getRecipient().getId(), debt.getRecipient().getName()),
        formatAmount(debt.getAmount().getAmount()));
  }

  private PaymentDto mapToDto(Payment payment) {
    return new PaymentDto(
        payment.getDescription(),
        formatAmount(payment.getAmount().getAmount()),
        payment.getDate().format(ISO_DATE_TIME),
        payment.getPayer().getName());
  }

  private String formatAmount(BigDecimal amount) {
    return EURO_FORMAT.format(amount);
  }
}
