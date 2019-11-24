package controllers;

import models.Greeting;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class HelloController extends Controller {

    public Result byName(String name) {
        Greeting hello = new Greeting("hello", name);
        return ok(Json.toJson(hello));
    }

    public Result anonymous() {
        return redirect(routes.HelloController.byName("anonymous"));
    }

    public Result helloView(Http.Request request, String name) {
        return ok(views.html.helloView.render(name, request));
    }
}
