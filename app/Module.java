import cache.PlayServerCache;
import cache.ServerCache;
import com.google.inject.AbstractModule;
import db.GreetingDAO;
import db.UserDAO;
import http.HttpRetryableClient;
import http.RetryableClient;
import services.GCloudTranslation;
import store.GreetingStore;
import store.UserStore;
import translate.Translation;

public class Module extends AbstractModule {

    protected void configure() {
        bind(UserStore.class).to(UserDAO.class);
        bind(GreetingStore.class).to(GreetingDAO.class);
        bind(RetryableClient.class).to(HttpRetryableClient.class);
        bind(Translation.class).to(GCloudTranslation.class);
        bind(ServerCache.class).to(PlayServerCache.class);
    }
}
