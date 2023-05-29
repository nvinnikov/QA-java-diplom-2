import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        Random random = new Random();
        this.email = "something" + random.nextInt(10000000) + "@yandex.ru";
        this.password = "password" + random.nextInt(10000000);
        CreateUser createUser = new CreateUser(email, password, "Nikita");
        Response responseCreate = UserClient.postApiAuthRegister(createUser);
        responseCreate.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        String responseString = responseCreate.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        this.accessToken = createUserResponse.getAccessToken();
    }

    @Test
    public void checkLoginUserResponseBodyTest() {
        LoginUser loginUser = new LoginUser(email, password);
        UserClient.postApiAuthLogin(loginUser).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void checkLoginUserNegativeResponseBodyTest() {
        Random random = new Random();
        String emailNegative = "something" + random.nextInt(10000000) + "@yandex.ru";
        String passwordNegative = "password" + random.nextInt(10000000);
        LoginUser loginUser = new LoginUser(emailNegative, passwordNegative);
        UserClient.postApiAuthLogin(loginUser).then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @After
    public void cleanUp() {
        UserClient.deleteApiAuthUser(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .body("message", equalTo("User successfully removed"))
                .and()
                .statusCode(202);
    }
}
