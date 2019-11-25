package db;

import models.Greeting;
import store.GreetingStore;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class TestGreetingDB implements GreetingStore {
    @Override
    public CompletionStage<Greeting> byGreeting(String greeting) {
        return null;
    }

    @Override
    public CompletionStage<Greeting> upsertUser(Greeting greeting) {
        return null;
    }

    @Override
    public CompletionStage<List<?>> all() {
        return null;
    }
}
