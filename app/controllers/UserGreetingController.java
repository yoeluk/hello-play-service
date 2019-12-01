package controllers;

import actions.UserGreetingAction;
import models.Greeting;
import models.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import services.UserGreetingService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@With(UserGreetingAction.class)
public class UserGreetingController extends Controller {

    @Inject
    UserGreetingService userGreetingService;

    @Inject
    HttpExecutionContext hec;

    public CompletionStage<Result> addUser(Http.Request request) {
        User user = Json.fromJson(request.body().asJson(), User.class);
        return userGreetingService.addUser(user)
                .thenApplyAsync(
                        persistedUser -> {
                            if (persistedUser.isPresent()) {
                                return ok(Json.parse("{\"msg\":\"user " + persistedUser.get().fullName + " was persisted\"}"));
                            } else {
                                return internalServerError();
                            }
                        }, hec.current()
                );
    }

    public CompletionStage<Result> userGreetings(Long id) {
        return userGreetingService.userGreetingForId(id)
                .thenApplyAsync(userGreeting ->
                        ok(Json.toJson(userGreeting)), hec.current());
    }

    public CompletionStage<Result> getUser(Optional<Long> maybeUserId) {
        if (maybeUserId.isPresent()) {
            long id = maybeUserId.get();
            return userGreetingService.userForId(id)
                    .thenApplyAsync(foundUser -> {
                        if (foundUser.isPresent()) {
                            return ok(Json.toJson(foundUser.get()));
                        } else {
                            return notFound("a user with id " + id + " was not found");
                        }}, hec.current());
        } else {
            return CompletableFuture.supplyAsync(
                    () -> badRequest("a userId is required and none were found in the request"),
                    hec.current()
            );
        }
    }

    public CompletionStage<Result> addGreeting(Http.Request request) {
        Greeting greeting = Json.fromJson(request.body().asJson(), Greeting.class);
        return userGreetingService.addGreeting(greeting)
                .thenApplyAsync(greeting1 -> ok(Json.toJson(greeting)), hec.current());
    }

    public CompletionStage<Result> allGreetings() {
        return userGreetingService.allGreetings()
                .thenApplyAsync(greetings -> ok(
                        Json.toJson(greetings.collect(Collectors.toList())))
                        , hec.current()
                );
    }
}