package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

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


}
