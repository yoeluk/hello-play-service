package com.dhl.playservice.controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class HelloController extends Controller {

    public Result byName(String name) {
        return ok(Json.toJson("hello " + name));
    }
}
