package services;

import models.UserGreeting;
import store.GreetingStore;
import store.UserStore;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletionStage;

public class UserService {

    @Inject
    UserService(UserStore userStore, GreetingStore greetingStore) {}

    public CompletionStage<UserGreeting> byUserId(long id) {
        return null;
    }
}
