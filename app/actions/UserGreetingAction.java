package actions;

import com.codahale.metrics.*;
import net.jodah.failsafe.FailsafeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.Futures;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.mvc.Http.Status.*;

public class UserGreetingAction extends play.mvc.Action.Simple {
    private final Logger logger = LoggerFactory.getLogger("application.UserGreetingAction");

    private final Meter requestsMeter;
    private final Timer responsesTimer;
    private final HttpExecutionContext ec;
    private final Futures futures;

    @Singleton
    @Inject
    public UserGreetingAction(MetricRegistry metrics, HttpExecutionContext ec, Futures futures) {
        this.ec = ec;
        this.futures = futures;
        this.requestsMeter = metrics.meter("requestsMeter");
        this.responsesTimer = metrics.timer(name(UserGreetingAction.class, "responsesTimer"));
        Slf4jReporter reporter = Slf4jReporter.forRegistry(metrics).build();
        reporter.start(10, TimeUnit.SECONDS);
        reporter.report();
    }

    public CompletionStage<Result> call(Http.Request request) {
        if (logger.isTraceEnabled()) {
            logger.trace("call: request = " + request);
        }

        requestsMeter.mark(); // count the request
        if (request.accepts("application/json")) {
            // start the timer
            final Timer.Context time = responsesTimer.time();
            return futures.timeout(doCall(request), 1L, TimeUnit.MINUTES)
                    .exceptionally(e -> (status(GATEWAY_TIMEOUT, "{\"errorMessage\":\"persistence timeout\"}")))
                    .whenComplete((r, e) ->
                            // stop the timer measuring the time it took
                            time.close());
        } else {
            return completedFuture(
                    status(NOT_ACCEPTABLE, "{\"errorMessage\":\"We only accept application/json\"}")
            );
        }
    }

    private CompletionStage<Result> doCall(Http.Request request) {

        return  // propagate the request to the next action
                delegate.call(request)
                // handle the returned result from the action chain propagation
                .handleAsync((result, e) -> {
                    if (e != null) {
                        if (e instanceof CompletionException) {
                            Throwable completionException = e.getCause();
                            // Failsafe isn't implemented in this POC
                            if (completionException instanceof FailsafeException) {
                                // this circuit breaker behaviour isn't implemented
                                // this is shown here as a possible behaviour that can be added
                                // by intercepting and composing actions
                                logger.error("Circuit breaker is open!", completionException);
                                return status(SERVICE_UNAVAILABLE, "Service has timed out");
                            } else {
                                // dummy behaviour
                                logger.error("Direct exception " + e.getMessage(), e);
                                return internalServerError();
                            }
                        } else {
                            // dummy behaviour
                            logger.error("Unknown exception " + e.getMessage(), e);
                            return internalServerError();
                        }
                    } else {
                        // successful result
                        return result;
                    }
        }, ec.current());
    }
}
