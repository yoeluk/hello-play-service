package actions;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import store.UserStore;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static play.shaded.oauth.org.apache.commons.codec.binary.Base64.decodeBase64;

public class Authorized extends Action.Simple {

    @Inject
    private UserStore userStore;

    @Inject
    HttpExecutionContext hec;

    public CompletionStage<Result> call(Http.Request request) {
        Optional<String> maybeAuthToken = request.getHeaders().get("Authorization");
        if (maybeAuthToken.isPresent()) {
            String authToken = maybeAuthToken.get();
            Stream<String> parsed = Arrays.stream(authToken.split(" "))
                    .skip(1).flatMap(encoded ->
                            Arrays.stream(new String(decodeBase64(encoded.getBytes()))
                                    .split(":")));
            Optional<String> maybePassword = parsed.skip(1).findFirst();
            Optional<String> maybeUsername = parsed.findFirst();
            if (maybePassword.isPresent() && maybeUsername.isPresent()) {
                String password = maybePassword.get();
                String username = maybeUsername.get();
                return isAuthorizedUser(username, password)
                        .thenComposeAsync(isAuthorized -> {
                            if (isAuthorized) {
                                return delegate.call(request);
                            } else {
                                return CompletableFuture.completedFuture(
                                        unauthorized("You are not authorized to access the requested resource"));
                            }
                        }, hec.current());
            } else {
                return CompletableFuture.completedFuture(badRequest());
            }
        } else {
            return CompletableFuture.completedFuture(badRequest());
        }
    }

    public CompletionStage<Boolean> isAuthorizedUser(String username, String password) {
        return userStore.findByUsername(username)
                .thenApplyAsync(maybeUser -> {
                    if (maybeUser.isPresent()) {
                        User user = maybeUser.get();
                        return BCrypt.checkpw(password, user.getCredentials().getPassword());
                    } else {
                        return false;
                    }
                });
    }
}
