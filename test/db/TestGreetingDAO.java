package db;

import lombok.Value;
import models.Greeting;
import store.GreetingStore;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@Value(staticConstructor = "of")
public class TestGreetingDAO implements GreetingStore {

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
    public CompletionStage<Optional<Greeting>> upsertGreeting(Greeting greeting) {
        return CompletableFuture.completedFuture(Optional.ofNullable(db.put(greeting.greeting, greeting)));
    }

    @Override
    public CompletionStage<Stream<Greeting>> all() {
        return CompletableFuture.completedFuture(db.values().stream());
    }
}
