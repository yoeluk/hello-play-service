import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import db.TestGreetingDB;
import db.TestUserDB;
import org.junit.After;
import org.junit.Before;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.Helpers;
import store.GreetingStore;
import store.UserStore;

import javax.inject.Inject;
import java.util.HashMap;

public class TestApplication {

    @Inject
    Application application;

    @Before
    public void setup() {
        Module testModule =
                new AbstractModule() {
                    @Override
                    public void configure() {
                        // Install custom test binding here
                        bind(UserStore.class).toInstance(TestUserDB.of(new HashMap<>()));
                        bind(GreetingStore.class).toInstance(TestGreetingDB.of(new HashMap<>()));
                    }
                };

        GuiceApplicationBuilder builder =
                new GuiceApplicationLoader()
                        .builder(new ApplicationLoader.Context(Environment.simple()))
                        .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);

        Helpers.start(application);
    }

    @After
    public void teardown() {
        Helpers.stop(application);
    }
}
