import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void checkCreateUserResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        CreateUser createUser = new CreateUser(email, "nikita", "Nikita");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post("/api/auth/register");

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void checkCreateUserAlreadyRegisteredResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        CreateUser createUser = new CreateUser(email, "nikita", "Nikita");

        Response response1 = given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post("/api/auth/register");

        response1.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        Response response2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post("/api/auth/register");

        response2.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    public void checkCreateUserWithoutFieldResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        CreateUser createUser = new CreateUser(email, "Nikita");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post("/api/auth/register");

        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}

