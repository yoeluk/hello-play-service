package services;

import akka.actor.ActorSystem;
import akka.pattern.CircuitBreaker;
import controllers.TranslateController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import translate.Translation;

import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class TranslateService implements Translation {

    private Logger log = LoggerFactory.getLogger(TranslateController.class);

    private ActorSystem actorSystem;

    private CircuitBreaker cb;

    @Inject
    WSClient ws;

    @Inject
    TranslateService(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
        this.cb = new CircuitBreaker(
                actorSystem.dispatcher(),
                actorSystem.scheduler(), 5,
                Duration.ofSeconds(10),
                Duration.ofMinutes(1)
        ).addOnOpenListener(this::notifyMeOnOpen);
    }

    public CompletionStage<WSResponse> translateMe(String text, String from, String to) {

        String token = actorSystem.settings().config().getString("hello-play-service.translate-api.bearer-token");

        String contentType = "application/json";
        String bearerToken = "Bearer " + token;

        return cb.callWithCircuitBreakerCS(() ->
                ws.url("https://translation.googleapis.com/language/translate/v2")
                        .addHeader("Content-Type", contentType)
                        .addHeader("Authorization", bearerToken)
                        .post("{ 'q': '" + text + "', 'source': '" + from + "', 'target': '" + to + "', 'format': 'text'}"));
    }

    public void notifyMeOnOpen() {
        log.warn("My CircuitBreaker is now open, and will not close for one minute");
    }
}
