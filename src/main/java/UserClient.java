import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    private final static String api_auth_register = "/api/auth/register";
    private final static String api_auth_login = "/api/auth/login";
    private final static String api_auth_user = "/api/auth/user";

    public UserClient() {
    }

    public static Response postApiAuthRegister(CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post(api_auth_register);
    }

    public static Response postApiAuthLogin(LoginUser loginUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post(api_auth_login);
    }

    public static Response deleteApiAuthUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .delete(api_auth_user);
    }

    public static Response getApiAuthUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get(api_auth_user);
    }

    public static Response getApiAuthUser() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(api_auth_user);
    }

    public static Response patchApiAuthUser(String accessToken, CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(createUser)
                .when()
                .patch(api_auth_user);
    }

    public static Response patchApiAuthUser(CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .patch(api_auth_user);
    }

}
