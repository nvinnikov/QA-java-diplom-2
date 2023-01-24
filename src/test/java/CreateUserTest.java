import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void checkCreateUserResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        CreateUser createUser = new CreateUser(email, "nikita", "Nikita");
        Response response = UserClient.postApiAuthRegister(createUser);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        this.accessToken = createUserResponse.getAccessToken();
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void checkCreateUserAlreadyRegisteredResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        CreateUser createUser = new CreateUser(email, "nikita", "Nikita");
        Response response = UserClient.postApiAuthRegister(createUser);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        this.accessToken = createUserResponse.getAccessToken();
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        UserClient.postApiAuthRegister(createUser).then().assertThat().body("success", equalTo(false))
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
        Response response = UserClient.postApiAuthRegister(createUser);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        this.accessToken = createUserResponse.getAccessToken();
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                    .and()
                    .body("message", equalTo("User successfully removed"))
                    .and()
                    .statusCode(202);
        }
    }
}
