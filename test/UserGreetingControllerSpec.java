import com.fasterxml.jackson.databind.JsonNode;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.HttpVerbs.GET;
import static play.mvc.Http.HttpVerbs.POST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.route;

@FixMethodOrder(MethodSorters.JVM)
public class UserGreetingControllerSpec extends TestApplication {

    @Test
    public void addUserTest() {
        JsonNode user = Json.parse("{\"fullName\":\"ariel smith\",\"id\":1234,\"email\":\"ariel.smith@me.com\"}");
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST)
                .bodyJson(user)
                .uri("/v1/ug/adduser");

        Result result = route(application, request);

        assertEquals(OK, result.status());
    }

    @Test
    public void getUserTest() {

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/v1/ug/getuser?id=1234");

        Result result = route(application, request);

        assertEquals(OK, result.status());
    }

    @Test
    public void allGreetingsTest() {

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/v1/ug/allgreetings");

        Result result = route(application, request);

        assertEquals(OK, result.status());
    }

    @Test
    public void userGreetingsTest() {

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/v1/ug/usergreetings/1234");

        Result result = route(application, request);

        assertEquals(OK, result.status());
    }
}
