package actions;

import controllers.routes;
import models.Constants;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Authenticated extends Action.Simple {

    public CompletionStage<Result> call(Http.Request request) {

        Optional<String> maybeUsername = request.session().getOptional(Constants.SESSION_USERID_KEY);

        if (maybeUsername.isPresent()) {
            return delegate.call(request);
        } else {
            return CompletableFuture.completedFuture(
                    redirect(routes.UserController.login())
                    .flashing("info", "You are logged out. Please login!")
                    .withNewSession()
            );
        }

    }
}
