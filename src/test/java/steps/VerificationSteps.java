
package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class VerificationSteps {

    @Step("Проверить успешное создание курьера")
    public void verifyCourierCreatedSuccessfully(Response response) {
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Проверить ошибку с кодом {code} и сообщением: {message}")
    public void verifyError(Response response, int code, String message) {
        response.then()
                .statusCode(code)
                .body("message", containsString(message));
    }

    @Step("Проверить успешную авторизацию")
    public void verifyAuthorizationSuccessful(Response response) {
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }


    @Step("Проверить Создался ли заказ")
    public void verifyCreateOrder(Response response , int expectedStatusCode ) {
        response.then()
                .statusCode(expectedStatusCode)
                .body("track", expectedStatusCode == 201 ? notNullValue() : not(notNullValue()));
    }
}