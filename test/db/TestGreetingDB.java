package db;

import lombok.Value;
import models.Greeting;
import store.GreetingStore;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Value(staticConstructor = "of")
public class TestGreetingDB implements GreetingStore {

    private HashMap<String, Greeting> db;

    @Override
    public CompletionStage<Optional<Greeting>> forGreeting(String greeting) {
        try {
            return CompletableFuture.completedFuture(Optional.of(db.get(greeting)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }

    @Override
    public CompletionStage<Greeting> upsertUser(Greeting greeting) {
        return CompletableFuture.completedFuture(db.put(greeting.greeting, greeting));
    }

    @Override
    public CompletionStage<Collection<Greeting>> all() {
        return CompletableFuture.completedFuture(db.values());
    }
}
