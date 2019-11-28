package store;

import models.Greeting;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public interface GreetingStore {
    CompletionStage<Optional<Greeting>> forGreeting(String greeting);
    CompletionStage<Greeting> upsertGreeting(Greeting greeting);
    CompletionStage<Stream<Greeting>> all();
}
