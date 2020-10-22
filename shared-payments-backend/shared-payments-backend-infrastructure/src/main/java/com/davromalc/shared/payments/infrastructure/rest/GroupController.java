package com.davromalc.shared.payments.infrastructure.rest;

import com.davromalc.shared.payments.domain.User;
import com.davromalc.shared.payments.infrastructure.rest.dto.FriendDto;
import com.davromalc.shared.payments.infrastructure.rest.dto.UserDto;
import com.davromalc.shared.payments.usecase.friends.AddFriendParams;
import com.davromalc.shared.payments.usecase.shared.UseCase;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpResponseFactory;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.simple.SimpleHttpResponseFactory;
import java.util.function.Function;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

@Controller("/api/group/")
class GroupController {

  private final UseCase<AddFriendParams, User> addFriend;

  private final HttpResponseFactory httpResponseFactory = SimpleHttpResponseFactory.INSTANCE;

  GroupController(@Named("transactionalAddFriendUseCase") UseCase<AddFriendParams, User> addFriend) {
    this.addFriend = addFriend;
  }

  @Post(uri = "user/{userId}", processes = MediaType.APPLICATION_JSON)
  HttpResponse<?> addFriend(@PathVariable("userId") Integer userId, @NotNull @Body FriendDto friend) {
    return addFriend.execute(new AddFriendParams(friend.getName(), Long.valueOf(userId)))
        .map(user -> new UserDto(user.getId(), user.getName()))
        .map(user -> httpResponseFactory.status(HttpStatus.CREATED, user))
        .fold(HttpResponse::badRequest, Function.identity());
  }
}
