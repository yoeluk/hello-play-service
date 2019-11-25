package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class UserGreetingController extends Controller {

    public Result addUser(Http.Request request) {
        return ok();
    }

}
