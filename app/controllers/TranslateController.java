package controllers;

import cache.ServerCache;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import translate.Translation;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class TranslateController extends Controller {

    @Inject
    HttpExecutionContext exc;

    @Inject
    Translation ts;

    @Inject
    ServerCache serverCache;

    public CompletionStage<Result> translateMe(String text, String from, String to) {
        String string = text + "&&" + from + "&&" + to;
        String key = String.valueOf(string.hashCode());
        return serverCache.getOrUpdateKey(
                key, () -> translate(text, from, to), 600)
                .thenApplyAsync(Results::ok, exc.current());
    }

    private CompletionStage<String> translate(String text, String from, String to) {
        return ts.translateMe(text, from, to)
                .thenApplyAsync(response -> {
                    if (response.getStatus() == 200) {
                        return response.getBody();
                    } else {
                        throw new RuntimeException(response.getStatusText());
                    }
                }, exc.current());
    }
}
