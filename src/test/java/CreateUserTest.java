import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.Random;

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
        Response response = UserClient.postApiAuthRegister(createUser);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        String accessToken = createUserResponse.getAccessToken();
        UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .body("message", equalTo("User successfully removed"))
                .and()
                .statusCode(202);
    }

    @Test
    public void checkCreateUserAlreadyRegisteredResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        CreateUser createUser = new CreateUser(email, "nikita", "Nikita");
        Response response = UserClient.postApiAuthRegister(createUser);
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        UserClient.postApiAuthRegister(createUser).then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        String accessToken = createUserResponse.getAccessToken();
        UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .body("message", equalTo("User successfully removed"))
                .and()
                .statusCode(202);
    }

    @Test
    public void checkCreateUserWithoutFieldResponseBodyTest() {
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        CreateUser createUser = new CreateUser(email, "Nikita");
        Response response = UserClient.postApiAuthRegister(createUser);
        response.then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
        String responseString = response.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        String accessToken = createUserResponse.getAccessToken();
        if(!Objects.equals(accessToken, null)){
            UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                    .and()
                    .body("message", equalTo("User successfully removed"))
                    .and()
                    .statusCode(202);
        }
    }
}
