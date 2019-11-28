package db;

import akka.actor.ActorSystem;
import models.User;
import play.db.jpa.JPAApi;
import scala.concurrent.ExecutionContextExecutor;
import store.UserStore;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserDAO implements UserStore {

    private final JPAApi jpaApi;

    private final String emName = "user";

    private ExecutionContextExecutor exc;

    @Inject
    UserDAO(ActorSystem actorSystem, JPAApi jpaApi) {
        this.exc = actorSystem.dispatchers().lookup("hello-play-service.database-dispatcher");
        this.jpaApi = jpaApi;
    }

    @Override
    public CompletionStage<Optional<User>> findById(long id) {
        return null;
    }

    @Override
    public CompletionStage<Optional<User>> upsertUser(User user) {
        return supplyAsync(() -> wrap(emName, em -> insert(em, user)), exc);
    }


    private Optional<User> insert(EntityManager em, User user) {
        try {
            em.persist(user);
            return Optional.of(user);
        } catch (Exception e) {
            // log the e or better
            return Optional.empty();
        }
    }

    private <T> T wrap(String emName, Function<EntityManager, T> function) {
        return jpaApi.withTransaction(emName, function);
    }
}
