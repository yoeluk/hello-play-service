package db;

import models.Greeting;
import store.GreetingStore;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class GreetingDB implements GreetingStore {

    @Override
    public CompletionStage<Optional<Greeting>> forGreeting(String greeting) {
        return null;
    }

    @Override
    public CompletionStage<Greeting> upsertUser(Greeting greeting) {
        return null;
    }

    @Override
    public CompletionStage<Collection<Greeting>> all() {
        return null;
    }
}
