import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import db.GreetingDB;
import db.UserDB;
import play.Environment;
import store.GreetingStore;
import store.UserStore;

public class Module extends AbstractModule {

    protected void configure() {
        bind(UserStore.class).to(UserDB.class);
        bind(GreetingStore.class).to(GreetingDB.class);
    }
}
