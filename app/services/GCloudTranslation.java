package services;

import akka.actor.ActorSystem;
import http.HttpRetryableClient;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import translate.Translation;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class GCloudTranslation implements Translation {

    @Inject
    private ActorSystem actorSystem;

    @Inject
    HttpRetryableClient httpRetryableClient;

    @Inject
    WSClient ws;

    public CompletionStage<WSResponse> translateMe(String text, String from, String to) {

        String token = actorSystem.settings().config().getString("hello-play-service.translate-api.bearer-token");

        String contentType = "application/json";
        String bearerToken = "Bearer " + token;

        WSRequest request = ws.url("https://translation.googleapis.com/language/translate/v2")
                .addHeader("Content-Type", contentType)
                .addHeader("Authorization", bearerToken)
                .setBody("{ 'q': '" + text + "', 'source': '" + from + "', 'target': '" + to + "', 'format': 'text'}")
                .setMethod("POST");

        return httpRetryableClient.withRetries(request::execute);
    }
}