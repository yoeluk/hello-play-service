package db;

import akka.actor.ActorSystem;
import models.Greeting;
import scala.concurrent.ExecutionContextExecutor;
import store.GreetingStore;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class GreetingDB implements GreetingStore {

    private HashMap<String, Greeting> db;

    private ExecutionContextExecutor exc;

    @Inject
    GreetingDB(ActorSystem actorSystem, HashMap<String, Greeting> db) {
        this.exc = actorSystem.dispatchers().lookup("service.greeting-dispatcher");
        this.db = db;
    }

    @Override
    public CompletionStage<Optional<Greeting>> forGreeting(String greeting) {
        return null;
    }

    @Override
    public CompletionStage<Greeting> upsertGreeting(Greeting greeting) {
        return null;
    }

    @Override
    public CompletionStage<Collection<Greeting>> all() {
        return CompletableFuture.supplyAsync(db::values, exc);
    }
}
