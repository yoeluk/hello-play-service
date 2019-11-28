package db;

import akka.actor.ActorSystem;
import models.Greeting;
import play.db.jpa.JPAApi;
import scala.concurrent.ExecutionContextExecutor;
import store.GreetingStore;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class GreetingDAO implements GreetingStore {

    private final JPAApi jpaApi;

    private final String emName = "greeting";

    private ExecutionContextExecutor exc;

    @Inject
    GreetingDAO(ActorSystem actorSystem, JPAApi jpaApi) {
        this.exc = actorSystem.dispatchers().lookup("hello-play-service.database-dispatcher");
        this.jpaApi = jpaApi;
    }

    @Override
    public CompletionStage<Optional<Greeting>> forGreeting(String greeting) {
        return null;
    }

    @Override
    public CompletionStage<Optional<Greeting>> upsertGreeting(Greeting greeting) {
        return supplyAsync(() -> wrap(emName, em -> insert(em, greeting)), exc);
    }

    @Override
    public CompletionStage<Stream<Greeting>> all() {
        return supplyAsync(() -> wrap(emName, this::list), exc);
    }

    private <T> T wrap(String emName, Function<EntityManager, T> function) {
        return jpaApi.withTransaction(emName, function);
    }

    private Optional<Greeting> insert(EntityManager em, Greeting greeting) {
        try {
            em.persist(greeting);
            return Optional.of(greeting);
        } catch (Exception e) {
            // log the e or better
            return Optional.empty();
        }
    }

    private Stream<Greeting> list(EntityManager em) {
        List<Greeting> greetings = em.createQuery("select p from Greeting p", Greeting.class).getResultList();
        return greetings.stream();
    }
}
