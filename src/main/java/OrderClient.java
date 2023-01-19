import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public OrderClient() {
    }

    public static Response postApiOrders(String accessToken, Ingredients ingredientsReq) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(ingredientsReq)
                .when()
                .post("/api/orders");
    }

    public static Response postApiOrders(Ingredients ingredientsReq) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientsReq)
                .when()
                .post("/api/orders");
    }

    public static Response getApiOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get("/api/orders");
    }

    public static Response getApiOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders");
    }
}
