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

    private final String emName;

    private ExecutionContextExecutor exc;

    @Inject
    GreetingDAO(ActorSystem actorSystem, JPAApi jpaApi) {
        this.exc = actorSystem.dispatchers().lookup("hello-play-service.database-dispatcher");
        this.jpaApi = jpaApi;
        this.emName = "greeting";
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
    public CompletionStage<Stream<Greeting>> all() {
        return supplyAsync(() -> wrap(emName, this::list), exc);
    }

    private <T> T wrap(String emName, Function<EntityManager, T> function) {
        return jpaApi.withTransaction(emName, function);
    }

    private Greeting insert(EntityManager em, Greeting greeting) {
        em.persist(greeting);
        return greeting;
    }

    private Stream<Greeting> list(EntityManager em) {
        List<Greeting> greetings = em.createQuery("select p from Greeting p", Greeting.class).getResultList();
        return greetings.stream();
    }
}
