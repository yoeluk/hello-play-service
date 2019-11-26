package db;

import models.User;
import store.UserStore;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class UserDB implements UserStore {

    @Override
    public CompletionStage<Optional<User>> findById(long id) {
        return null;
    }

    @Override
    public CompletionStage<User> upsertUser(User user) {
        return null;
    }
}
