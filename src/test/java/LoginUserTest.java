import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private String email;
    private String password;

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
    }

    @Test
    public void checkLoginUserResponseBodyTest() {
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
    }

    @Test
    public void checkLoginUserNegativeResponseBodyTest() {
        Random random = new Random();
        String emailNegative = "something" + random.nextInt(10000000) + "@yandex.ru";
        String passwordNegative = "password" + random.nextInt(10000000);
        LoginUser loginUser = new LoginUser(emailNegative, passwordNegative);
        Response responseLogin = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post("/api/auth/login");

        responseLogin.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}