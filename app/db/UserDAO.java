package db;

import akka.actor.ActorSystem;
import models.Credentials;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.jpa.JPAApi;
import scala.concurrent.ExecutionContextExecutor;
import store.UserStore;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserDAO implements UserStore {

    private final JPAApi jpaApi;

    private final String emName = "user";

    private ExecutionContextExecutor exc;

    private Logger log = LoggerFactory.getLogger(User.class);

    @Inject
    UserDAO(ActorSystem actorSystem, JPAApi jpaApi) {
        this.exc = actorSystem.dispatchers().lookup("hello-play-service.database-dispatcher");
        this.jpaApi = jpaApi;
    }

    @Override
    public CompletionStage<Optional<User>> findById(long id) {
        return supplyAsync(() -> wrap(emName, em -> byId(em, id)));
    }

    @Override
    public CompletionStage<Optional<User>> findByUsername(String username) {
        return supplyAsync(() -> wrap(emName, em -> byUsername(em, username)));
    }

    @Override
    public CompletionStage<Optional<User>> upsertUser(User user) {
        secureUserPassword(user);
        user.getCredentials().setUser(user);
        return supplyAsync(() -> wrap(emName, em -> insert(em, user)), exc);
    }

    public Optional<User> byUsername(EntityManager em, String username) {
        TypedQuery<User> q = em.createQuery("select u from User u where u.username=:username", User.class);
        q.setParameter("username", username);
        try {
            return q.getResultList().stream().findFirst();
        } catch (Exception e) {
            // log e or better
            return Optional.empty();
        }
    }

    public Optional<User> byId(EntityManager em, long id) {
        try {
            return Optional.of(em.find(User.class, id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<User> insert(EntityManager em, User user) {
        try {
            em.persist(user);
            user.setCredentials(null); // remove the credentials after persistence
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    private void secureUserPassword(User user) {
        Credentials creds = user.getCredentials();
        String passwordHash = BCrypt.hashpw(creds.getPassword(), BCrypt.gensalt(8));
        creds.setPassword(passwordHash);
        user.setCredentials(creds);
    }

    private <T> T wrap(String emName, Function<EntityManager, T> function) {
        return jpaApi.withTransaction(emName, function);
    }
}
