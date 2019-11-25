package controllers;

import models.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

public class HelloController extends Controller {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Result byName(String name) {
        logger.debug("saying hello to " + name);
        Greeting hello = new Greeting("hello", name);
        return ok(Json.toJson(hello));
    }

    public Result anonymous() {
        return redirect(routes.HelloController.byName("anonymous"));
    }

    public Result helloView(Http.Request request, String name) {
        return ok(views.html.helloView.render(name, request));
    }

    @BodyParser.Of(BodyParser.Text.class)
    public Result addGreeting(Http.Request request) {
        String greeting = request.body().asText();
        Greeting.addGreeting(greeting);
        return ok("greeting " + greeting + " added");
    }

    public Result allGreetings() {
        List<String> grts = Greeting.allGreetings();
        return ok(Json.toJson(grts));
    }
}
