package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class AuthorizationSteps   {


    @Step("Авторизация курьера")
    public Response authorizationCourier(String loginBody) {
       return given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .log().all()
                .when()
                .post("/api/v1/courier/login")
                .then()
                .log().all()
                .extract().response();

    }
    @Step("Получить ID курьера по логину и паролю")
    public Integer getCourierId(String login, String password) {
        String loginBody = String.format("{\"login\":\"%s\",\"password\":\"%s\"}", login, password);

        Response loginResponse = authorizationCourier(loginBody);

        if (loginResponse.getStatusCode() == 200) {
            return loginResponse.jsonPath().getInt("id");
        } else {
            System.out.println("Не удалось получить ID курьера. Статус: " + loginResponse.getStatusCode());
            return null;
        }
    }


}
