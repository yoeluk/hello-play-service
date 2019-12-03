package controllers;

import actions.Authenticated;
import actions.Authorized;
import models.Constants;
import models.LoginUser;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.typedmap.TypedKey;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.With;
import store.UserStore;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller {

    @Inject
    private FormFactory formFactory;

    @Inject
    private MessagesApi messagesApi;

    @Inject
    private UserStore userStore;

    @Inject
    HttpExecutionContext hec;

    public Result login(Http.Request request) {
        Form<LoginUser> userForm = formFactory.form(LoginUser.class);
        Messages messages = messagesApi.preferred(request);
        return ok(views.html.userLogin.render(userForm, request, messages));
    }

    public CompletionStage<Result> loginAttempt(Http.Request request) {
        Form<LoginUser> result = formFactory.form(LoginUser.class)
                .bindFromRequest(request);

        if (result.hasGlobalErrors()) {
            return CompletableFuture.completedFuture(
                    badRequest(views.html.userLogin.render(
                            result, request, messagesApi.preferred(request))));
        } else {
            LoginUser loginUser = result.get();
            return userStore.findByUsername(loginUser.getUsername())
                    .thenApplyAsync(maybeStoredUser -> {
                        if (maybeStoredUser.isPresent()) {
                            User storedUser = maybeStoredUser.get();
                            String plainPassword = loginUser.getPassword();
                            String encodedPassword = storedUser.getCredentials().getPassword();
                            if (BCrypt.checkpw(plainPassword, encodedPassword)) {
                                Map<String, String> session = new HashMap<>();
                                session.put(Constants.SESSION_USERNAME_KEY, storedUser.getUsername());
                                session.put(Constants.SESSION_USERID_KEY, storedUser.getId().toString());
                                session.put(Constants.SESSION_USEREMAIL_KEY, storedUser.getEmail());
                                return redirect(routes.UserController.landingPage())
                                        .flashing("info", "You are logged in.")
                                        .withSession(session);
                            } else {
                                return redirect(routes.UserController.login())
                                        .flashing("error", "Login failed!")
                                        .withNewSession();
                            }
                        } else {
                            return redirect(routes.UserController.login())
                                    .flashing("error", "The user couldn't be found. Please try again or signup!")
                                    .withNewSession();
                        }
                    }, hec.current());
        }
    }

    @With(Authenticated.class)
    public Result landingPage(Http.Request request) { // the page is protected; the user need to be authenticated
        return ok(views.html.landingPage.render(
                routes.UserController.logout(), request.flash()));
    }

    public Result logout() {
        return redirect(routes.UserController.login())
                .flashing("info", "You are logged out.")
                .withNewSession();
    }

    @With(Authorized.class)
    public Result moreHoney(Http.Request request) {
        Optional<String> maybeUsername = request.attrs().getOptional(Constants.authUsername);
        if (maybeUsername.isPresent()) {
            String username = maybeUsername.get();
            return ok(Json.parse("{\"member\":\"" + username + "\",\"pot\":\"honey\",\"weight\":\"50g\"}"));
        } else {
            return unauthorized();
        }

    }
}
