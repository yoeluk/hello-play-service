package controllers;

import actions.UserGreetingAction;
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
                        persistedUser -> ok(Json.parse("{\"msg\":\"user " + user.fullName + " was persisted\"}")),
                        hec.current()
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
                    .thenApplyAsync(user ->
                            ok(Json.toJson(user)), hec.current());
        } else {
            return CompletableFuture.supplyAsync(
                    () -> badRequest("a userId is required and none were found in the request"),
                    hec.current()
            );
        }
    }

    public CompletionStage<Result> allGreetings() {
        return userGreetingService.allGreetings()
                .thenApplyAsync(greetings -> ok(Json.toJson(greetings)), hec.current());
    }
}