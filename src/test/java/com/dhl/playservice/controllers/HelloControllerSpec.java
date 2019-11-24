package com.dhl.playservice.controllers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.core.j.JavaResultExtractor;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json.*;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.GET;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.route;

public class HelloControllerSpec {

    @Inject
    Application application;

    @Before
    public void setup() {
        Module testModule =
                new AbstractModule() {
                    @Override
                    public void configure() {
                        // Install custom test binding here
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


    @Test
    @BodyParser.Of(BodyParser.Json.class)
    public void testBadRoute() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/from/me");

        Result result = route(application, request);

        assertEquals(OK, result.status());

        Greeting hello = new Greeting("hello", "me");
        Greeting responseGreeting = Json.fromJson(
                Json.parse(Helpers.contentAsString(result)),
                Greeting.class);

        assertEquals(hello.name, responseGreeting.name);
        assertEquals(hello.greeting, responseGreeting.greeting);
    }
}
