package http;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;

public interface RetryableClient {
    <R> CompletionStage<R> withRetries(Callable<CompletionStage<R>> request);
}
