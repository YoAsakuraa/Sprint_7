package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import testClass.CourierTestData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierSteps {

    @Step("Создание валидного курьера")
    public Response createCourier(String body) {
        if (body == null) {
            body = CourierTestData.generateUniqueBody();
        }
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all() // логируем запрос
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all() // логируем ответ
                .extract()
                .response();
    }

    @Step("Создание курьера со случайными данными")
    public Response createCourier() {
        return createCourier(null);
    }

    @Step("Создание курьера с одним и тем же логином")
    public Response createDuplicateCourier() {
        String body = CourierTestData.generateUniqueBody();
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all() // логируем запрос
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all()// логируем ответ
                .statusCode(201);



      return   given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Создание курьера без логина")
    public Response createCourierWithoutLogin() {
        String body = CourierTestData.generateBodyWithoutLogin();
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all() // логируем запрос
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all() // логируем ответ
                .extract()
                .response();
    }

    @Step("Создание курьера без пароля")
    public Response createCourierWithoutPassword() {
        String body = CourierTestData.generateBodyWithoutPassword();
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all() // логируем запрос
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all() // логируем ответ
                .extract()
                .response();
    }

    @Step("Создание курьера без имени")
    public Response createCourierWithoutFirstName() {
        String body = CourierTestData.generateBodyWithoutFirstName();
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .log().all() // логируем запрос
                .when()
                .post("/api/v1/courier")
                .then()
                .log().all() // логируем ответ
                .extract()
                .response();
    }


    @Step("Удалить курьера по ID: {courierId}")
    public void deleteCourierById(Integer courierId) {
        try {
            given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .delete("/api/v1/courier/" + courierId)
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body("ok", equalTo(true));

            System.out.println("✅ Курьер с ID " + courierId + " успешно удален");
        } catch (Exception e) {
            System.out.println("❌ Ошибка при удалении курьера с ID " + courierId + ": " + e.getMessage());
        }
    }


    @Step("Удалить курьера с логином: {login}")
    public void deleteCourierByLogin(String login, String password) {
        try {
            String loginBody = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password);

            Response loginResponse = given()
                    .contentType(ContentType.JSON)
                    .body(loginBody)
                    .log().all()
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .log().all()
                    .extract()
                    .response();

            if (loginResponse.getStatusCode() == 200) {
                Integer courierId = loginResponse.jsonPath().getInt("id");
                deleteCourierById(courierId);
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при удалении курьера " + login + ": " + e.getMessage());
        }
    }


}
