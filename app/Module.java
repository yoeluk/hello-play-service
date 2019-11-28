import com.google.inject.AbstractModule;
import db.GreetingDAO;
import db.UserDAO;
import store.GreetingStore;
import store.UserStore;

public class Module extends AbstractModule {

    protected void configure() {
        bind(UserStore.class).to(UserDAO.class);
        bind(GreetingStore.class).to(GreetingDAO.class);
    }
}
