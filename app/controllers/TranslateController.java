package controllers;

import akka.actor.ActorSystem;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.ws.*;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class TranslateController extends Controller {

    @Inject
    WSClient ws;

    @Inject
    HttpExecutionContext exc;

    @Inject
    ActorSystem actorSystem;

    public CompletionStage<Result> translateMe(String text, String from, String to) {

        String token = actorSystem.settings().config().getString("hello-play-service.translate-api.bearer-token");

        String contentType = "application/json";
        String bearerToken = "Bearer " + token;

        return ws.url("https://translation.googleapis.com/language/translate/v2")
                .addHeader("Content-Type", contentType)
                .addHeader("Authorization", bearerToken)
                .post("{ 'q': '" + text + "', 'source': '" + from + "', 'target': '" + to + "', 'format': 'text'}")
                .thenApplyAsync(response -> {
                    if (response.getStatus() == 200) {
                        return ok(response.getBody());
                    } else {
                        return internalServerError(response.getStatusText());
                    }
                }, exc.current());
    }
}
