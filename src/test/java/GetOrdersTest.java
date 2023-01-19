import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;

public class GetOrdersTest {
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        String password = "password" + random.nextInt(10000000);
        CreateUser createUser = new CreateUser(email, password, "Nikita");
        Response responseCreate = UserClient.postApiAuthRegister(createUser);
        responseCreate.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        String responseString = responseCreate.body().asString();
        Gson gson = new Gson();
        CreateUserResponse createUserResponse = gson.fromJson(responseString, CreateUserResponse.class);
        this.accessToken = createUserResponse.getAccessToken();

        Ingredients ingredientsReq = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        OrderClient.postApiOrders(accessToken, ingredientsReq).then().assertThat()
                .statusCode(200);
    }

    @Test
    public void checkGetOrdersResponseBodyTest() {
        OrderClient.getApiOrders(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void checkGetOrdersNoAuthResponseBodyTest() {
        OrderClient.getApiOrders().then().assertThat().body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
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
