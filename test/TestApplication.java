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
                        bind(UserStore.class).to(TestUserDB.class);
                        bind(GreetingStore.class).to(TestGreetingDB.class);
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
