import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    private String email;
    private String password;
    private String name;

    private String AccessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "password" + random.nextInt(10000000);
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
    }

    @Test
    public void checkUpdateUserResponseBodyTest() {
        Response responseGet = given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", AccessToken)
                .and()
                .when()
                .get("/api/auth/user");
        responseGet.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        Random random = new Random();
        this.email = "somebody" + random.nextInt(10000000) + "@yandex.ru";
        this.name = "somebody" + random.nextInt(10000000);
        this.password = "password" + random.nextInt(10000000);
        CreateUser createUser = new CreateUser(email, password, name);
        Response responsePatch = given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", AccessToken)
                .and()
                .body(createUser)
                .and()
                .when()
                .patch("/api/auth/user");
        responsePatch.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void checkUpdateUserNoAuthResponseBodyTest() {
        Response responseGet = given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/auth/user");
        responseGet.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);

        Random random = new Random();
        this.email = "somebody" + random.nextInt(10000000) + "@yandex.ru";
        this.name = "somebody" + random.nextInt(10000000);
        this.password = "password" + random.nextInt(10000000);
        CreateUser createUser = new CreateUser(email, password, name);
        Response responsePatch = given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .and()
                .when()
                .patch("/api/auth/user");
        responsePatch.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
