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
    private final Logger logger = LoggerFactory.getLogger("application.ApiAction");

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

        requestsMeter.mark();
        if (request.accepts("application/json")) {
            final Timer.Context time = responsesTimer.time();
            return futures.timeout(doCall(request), 1L, TimeUnit.SECONDS)
                    .exceptionally(e -> (status(GATEWAY_TIMEOUT, "{\"errorMessage\":\"persistence timeout\"}")))
                    .whenComplete((r, e) -> time.close());
        } else {
            return completedFuture(
                    status(NOT_ACCEPTABLE, "{\"errorMessage\":\"We only accept application/json\"}")
            );
        }
    }

    private CompletionStage<Result> doCall(Http.Request request) {
        return delegate.call(request).handleAsync((result, e) -> {
            if (e != null) {
                if (e instanceof CompletionException) {
                    Throwable completionException = e.getCause();
                    if (completionException instanceof FailsafeException) {
                        logger.error("Circuit breaker is open!", completionException);
                        return status(SERVICE_UNAVAILABLE, "Service has timed out");
                    } else {
                        logger.error("Direct exception " + e.getMessage(), e);
                        return internalServerError();
                    }
                } else {
                    logger.error("Unknown exception " + e.getMessage(), e);
                    return internalServerError();
                }
            } else {
                return result;
            }
        }, ec.current());
    }
}
