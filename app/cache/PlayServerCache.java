package cache;

import play.cache.AsyncCacheApi;

import javax.inject.Inject;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;

public class PlayServerCache implements ServerCache {

    @Inject
    AsyncCacheApi asyncCacheApi;

    public CompletionStage<String> getOrUpdateKey(String key, Callable<CompletionStage<String>> callable, int expiry) {
        return asyncCacheApi.getOrElseUpdate(key, callable, expiry);
    }
}
