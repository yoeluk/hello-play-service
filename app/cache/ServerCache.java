package cache;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;

public interface ServerCache {
    CompletionStage<String> getOrUpdateKey(String key, Callable<CompletionStage<String>> callable, int expiry);
}
