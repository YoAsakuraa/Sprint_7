package Practikum;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;

public class OrdersListTest {
    @BeforeEach
    public void setUp() {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.socket.timeout", 10000)
                );
    }

    @Test
    @Description("Получение списка заказов")
    public void shouldGetOrdersList() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(200)
                .body("orders", not(empty())) // проверяем что массив orders не пустой
                .body("orders.id", everyItem(notNullValue())); // проверяем что у каждого заказа есть ID
    }
}
