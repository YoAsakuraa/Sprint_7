package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import testClass.CourierTestData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OrderCreationSteps {



    @Step("Создание заказа")
    public Response createOrder(String body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all() // логируем запрос
                .when()
                .post("/api/v1/orders")
                .then()
                .log().all() // логируем ответ
                .extract()
                .response();
    }

    @Step("Отмена заказа с track: {trackId}")
    public void cancelOrder(int trackId) {
        String body = String.format("{\"track\": %d}", trackId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .put("/api/v1/orders/cancel")
                .then()
                .log().all()
                .statusCode(200)
                .body("ok", equalTo(true));
    }


}
