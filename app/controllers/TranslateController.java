package controllers;

import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.ws.*;
import services.TranslateService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class TranslateController extends Controller {

    @Inject
    HttpExecutionContext exc;

    @Inject
    TranslateService ts;

    public CompletionStage<Result> translateMe(String text, String from, String to) {
        return ts.translateMe(text, from, to)
                .thenApplyAsync(response -> {
                    if (response.getStatus() == 200) {
                        return ok(response.getBody());
                    } else {
                        return internalServerError(response.getStatusText());
                    }
                }, exc.current());
    }
}
