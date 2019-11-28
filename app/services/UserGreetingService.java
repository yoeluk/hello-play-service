package services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import models.Greeting;
import models.User;
import models.UserGreeting;
import store.GreetingStore;
import store.UserStore;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor(
        access = AccessLevel.PRIVATE,
        onConstructor = @__({ @Inject })
)
public class UserGreetingService {
    GreetingStore greetingStore;
    UserStore userStore;

    public CompletionStage<Optional<UserGreeting>> userGreetingForId(long id) {
        CompletionStage<Optional<User>> futureUser = userForId(id);
        CompletionStage<Stream<Greeting>> futureGreetings = greetingStore.all();
        return futureUser
                .thenCompose(maybeUser -> {
                    if (maybeUser.isPresent()) {
                        User user = maybeUser.get();
                        return futureGreetings.thenApply(allGreetings ->
                                Optional.of(new UserGreeting(user, allGreetings.collect(Collectors.toList())))
                        );
                    } else {
                        return CompletableFuture.completedFuture(Optional.empty());
                    }
                });
    }

    public CompletionStage<Optional<User>> addUser(User user) {
        return userStore.upsertUser(user);
    }

    public CompletionStage<Optional<User>> userForId(long id) {
        return userStore.findById(id);
    }

    public CompletionStage<Optional<Greeting>> addGreeting(Greeting greeting) {
        return greetingStore.upsertGreeting(greeting);
    }

    public CompletionStage<Stream<Greeting>> allGreetings() {
        return greetingStore.all();
    }
}
