package store;

import models.Greeting;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface GreetingStore {
    CompletionStage<Greeting> byGreeting(String greeting);
    CompletionStage<Greeting> upsertUser(Greeting greeting);
    CompletionStage<List<?>> all();
}
