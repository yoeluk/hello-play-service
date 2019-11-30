package translate;

import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;

public interface Translation {
    CompletionStage<WSResponse> translateMe(String text, String from, String to);
}
