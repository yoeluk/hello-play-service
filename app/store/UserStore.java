package store;

import models.User;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface UserStore {
   CompletionStage<Optional<User>> findById(long id);
   CompletionStage<Optional<User>> upsertUser(User user);
}
