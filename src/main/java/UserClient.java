import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    public UserClient() {
    }

    public static Response postApiAuthRegister(CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post("/api/auth/register");
    }

    public static Response postApiAuthLogin(LoginUser loginUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post("/api/auth/login");
    }

    public static Response deleteApiAuthUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }

    public static Response getApiAuthUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get("/api/auth/user");
    }

    public static Response getApiAuthUser() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/auth/user");
    }

    public static Response patchApiAuthUser(String accessToken, CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(createUser)
                .when()
                .patch("/api/auth/user");
    }

    public static Response patchApiAuthUser(CreateUser createUser) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .patch("/api/auth/user");
    }

}
