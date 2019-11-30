package http;

import akka.actor.ActorSystem;
import akka.pattern.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;

public class HttpRetryableClient implements RetryableClient {

    private Logger log = LoggerFactory.getLogger(HttpRetryableClient.class);

    private CircuitBreaker cb;

    @Inject
    HttpRetryableClient(ActorSystem actorSystem) {
        this.cb = new CircuitBreaker(
                actorSystem.dispatcher(),
                actorSystem.scheduler(), 5,
                Duration.ofSeconds(10),
                Duration.ofMinutes(1)
        ).addOnOpenListener(this::notifyMeOnOpen);
    }

    public <R> CompletionStage<R> withRetries(Callable<CompletionStage<R>> request) {
        return cb.callWithCircuitBreakerCS(request);
    }

    public void notifyMeOnOpen() {
        log.warn("My CircuitBreaker is now open, and will not close for one minute");
    }
}
