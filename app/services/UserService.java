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

@AllArgsConstructor(
        access = AccessLevel.PRIVATE,
        onConstructor = @__({ @Inject })
)
public class UserService {
    UserStore userStore;
    GreetingStore greetingStore;

    public CompletionStage<Optional<UserGreeting>> userGreetingForId(long id) {
        CompletionStage<Optional<User>> futureUser = userForId(id);
        CompletionStage<Collection<Greeting>> futureGreetings = greetingStore.all();
        return futureUser
                .thenCompose(maybeUser -> {
                    if (maybeUser.isPresent()) {
                        User user = maybeUser.get();
                        return futureGreetings.thenApply(allGreetings ->
                                Optional.of(new UserGreeting(user, new ArrayList<>(allGreetings)))
                        );
                    } else {
                        return CompletableFuture.completedFuture(Optional.empty());
                    }
                });
    }

    public CompletionStage<User> addUser(User user) {
        return userStore.upsertUser(user);
    }

    public CompletionStage<Optional<User>> userForId(long id) {
        return userStore.findById(id);
    }

    public CompletionStage<Collection<Greeting>> allGreetings() {
        return greetingStore.all();
    }
}
