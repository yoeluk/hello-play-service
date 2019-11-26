import com.google.inject.AbstractModule;
import db.GreetingDB;
import db.UserDB;
import models.Greeting;
import store.GreetingStore;
import store.UserStore;

import java.util.HashMap;

public class Module extends AbstractModule {

    protected void configure() {
        bind(HashMap.class).toInstance(new HashMap<String, Greeting>());
        bind(UserStore.class).to(UserDB.class);
        bind(GreetingStore.class).to(GreetingDB.class);
    }
}
