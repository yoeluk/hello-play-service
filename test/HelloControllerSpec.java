import com.fasterxml.jackson.databind.JsonNode;
import models.Greeting;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.GET;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.route;

public class HelloControllerSpec extends TestApplication {

    @Test
    public void testHelloGreeting() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/v0/from/me");

        Result result = route(application, request);

        assertEquals(OK, result.status());

        Greeting hello = new Greeting("hello", "me");
        Greeting responseGreeting = Json.fromJson(
                Json.parse(Helpers.contentAsString(result)), Greeting.class);

        assertEquals(hello, responseGreeting);
    }

    @Test
    public void testGreetingStore() {
        Http.RequestBuilder morningRequest = Helpers.fakeRequest()
                .method(POST)
                .bodyText("good morning")
                .uri("/v0/api/addgreeting");

        Result morningResult = route(application, morningRequest);

        assertEquals(OK, morningResult.status());

        Http.RequestBuilder helloRequest = Helpers.fakeRequest()
                .method(POST)
                .bodyText("hello")
                .uri("/v0/api/addgreeting");

        Result helloResult = route(application, helloRequest);

        assertEquals(OK, helloResult.status());

        Http.RequestBuilder greetingsRequest = Helpers.fakeRequest()
                .method(GET)
                .uri("/v0/api/allgreetings");

        Result allResult = route(application, greetingsRequest);

        List<JsonNode> expected = new ArrayList<>();
        expected.add(Json.toJson("good morning"));
        expected.add(Json.toJson("hello"));

        JsonNode found = Json.parse(Helpers.contentAsString(allResult));

        assertEquals(Json.toJson(expected), found);
    }
}
