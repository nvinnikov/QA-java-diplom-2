import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetOrdersTest {
    private String AccessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
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
        this.AccessToken = loginUserResponse.getAccessToken();

        Ingredients ingredientsReq = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));

        Response responseOrders = given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", AccessToken)
                .and()
                .body(ingredientsReq)
                .when()
                .post("/api/orders");
        responseOrders.then().assertThat()
                .statusCode(200);

    }

    @Test
    public void checkGetOrdersResponseBodyTest() {
        Response responseOrders = given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", AccessToken)
                .when()
                .get("/api/orders");
        responseOrders.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    public void checkGetOrdersNoAuthResponseBodyTest() {
        Response responseOrders = given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/orders");
        responseOrders.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

}
