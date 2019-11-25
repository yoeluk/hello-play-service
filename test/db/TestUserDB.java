package db;

import models.User;
import store.UserStore;

import java.util.concurrent.CompletionStage;

public class TestUserDB implements UserStore {
    @Override
    public CompletionStage<User> findById(long id) {
        return null;
    }

    @Override
    public CompletionStage<User> upsertUser(User user) {
        return null;
    }
}
