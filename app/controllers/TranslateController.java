package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import translate.Translation;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class TranslateController extends Controller {

    private Logger log = LoggerFactory.getLogger(TranslateController.class);

    @Inject
    HttpExecutionContext exc;

    @Inject
    Translation ts;

    public CompletionStage<Result> translateMe(String text, String from, String to) {
        return ts.translateMe(text, from, to)
                .thenApplyAsync(response -> {
                    if (response.getStatus() == 200) {
                        return ok(response.getBody());
                    } else {
                        log.error(response.getStatusText());
                        return internalServerError(response.getStatusText());
                    }
                }, exc.current());
    }
}
