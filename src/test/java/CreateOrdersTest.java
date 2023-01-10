import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateOrdersTest {

    private final List<String> ingridients;
    private final int statusCode;

    public CreateOrdersTest(List<String> ingridients, int statusCode, String testsNames) {
        this.ingridients = ingridients;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters(name = "{2}")
    public static Object[][] params() {
        return new Object[][]{
                {List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"), 200, "с ингредиентами"},
                {List.of(), 400, "без ингредиентов"},
                {List.of("no", "qq"), 500, "с неверным хешем ингредиентов"},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void checkCreateOrdersResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "password" + random.nextInt(10000000);
        CreateUser createUser = new CreateUser(email, password, "Nikita");
        Response responseCreate = given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post("/api/auth/register");

        responseCreate.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        LoginUser loginUser = new LoginUser(email, password);

        Response responseLogin = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post("/api/auth/login");
        responseLogin.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        String responseString = responseLogin.body().asString();
        Gson gson = new Gson();
        LoginUserResponse loginUserResponse = gson.fromJson(responseString, LoginUserResponse.class);
        String AccessToken = loginUserResponse.getAccessToken();

        Ingredients ingredientsReq = new Ingredients(ingridients);

        Response responseOrders = given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", AccessToken)
                .and()
                .body(ingredientsReq)
                .when()
                .post("/api/orders");

        responseOrders.then().assertThat()
                .statusCode(statusCode);
    }

    @Test
    public void checkCreateOrdersNoAuthResponseBodyTest() {
        Ingredients ingredientsReq = new Ingredients(ingridients);

        Response responseOrders = given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientsReq)
                .when()
                .post("/api/orders");

        responseOrders.then().assertThat()
                .statusCode(statusCode);
    }
}
