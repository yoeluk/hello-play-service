package store;

import models.User;

import java.util.concurrent.CompletionStage;

public interface UserStore {
   CompletionStage<User> findById(long id);
   CompletionStage<User> upsertUser(User user);
}
