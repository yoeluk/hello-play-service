package db;

import akka.actor.ActorSystem;
import models.User;
import play.db.jpa.JPAApi;
import scala.concurrent.ExecutionContextExecutor;
import store.UserStore;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class UserDAO implements UserStore {

    private final JPAApi jpaApi;

    private final String emName;

    private ExecutionContextExecutor exc;

    @Inject
    UserDAO(ActorSystem actorSystem, JPAApi jpaApi) {
        this.exc = actorSystem.dispatchers().lookup("hello-play-service.database-dispatcher");
        this.jpaApi = jpaApi;
        this.emName = "user";
    }

    @Override
    public CompletionStage<Optional<User>> findById(long id) {
        return null;
    }

    @Override
    public CompletionStage<User> upsertUser(User user) {
        return null;
    }
}
