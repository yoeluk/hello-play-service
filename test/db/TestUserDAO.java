package db;

import lombok.Value;
import models.User;
import store.UserStore;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Value(staticConstructor = "of")
public class TestUserDAO implements UserStore {

    private HashMap<Long, User> db;

    @Override
    public CompletionStage<Optional<User>> findById(long id) {
        try {
            return CompletableFuture.completedFuture(Optional.of(db.get(id)));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }

    @Override
    public CompletionStage<Optional<User>> findByUsername(String username) {
        return null;
    }

    @Override
    public CompletionStage<Optional<User>> upsertUser(User user) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(db.put(user.id, user)));
    }
}
