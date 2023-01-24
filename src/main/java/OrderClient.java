import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private final static String API_ORDERS = "/api/orders";

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
                .post(API_ORDERS);
    }

    public static Response postApiOrders(Ingredients ingredientsReq) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(ingredientsReq)
                .when()
                .post(API_ORDERS);
    }

    public static Response getApiOrders(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get(API_ORDERS);
    }

    public static Response getApiOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(API_ORDERS);
    }
}
