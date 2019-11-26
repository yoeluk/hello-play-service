package store;

import models.Greeting;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface GreetingStore {
    CompletionStage<Optional<Greeting>> forGreeting(String greeting);
    CompletionStage<Greeting> upsertUser(Greeting greeting);
    CompletionStage<Collection<Greeting>> all();
}
