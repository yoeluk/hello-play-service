import com.google.inject.AbstractModule;
import db.GreetingDAO;
import db.UserDAO;
import services.GCloudTranslation;
import store.GreetingStore;
import store.UserStore;
import translate.Translation;

public class Module extends AbstractModule {

    protected void configure() {
        bind(UserStore.class).to(UserDAO.class);
        bind(GreetingStore.class).to(GreetingDAO.class);
        bind(Translation.class).to(GCloudTranslation.class);
    }
}
