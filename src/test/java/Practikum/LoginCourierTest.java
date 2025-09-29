package Practikum;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import steps.AuthorizationSteps;
import steps.CourierSteps;
import steps.VerificationSteps;
import testClass.CourierTestData;


public class LoginCourierTest {
    private String login;
    private String password;
    private String firstName;
    private Integer createdCourierId;
    private final CourierSteps courierSteps = new CourierSteps();
    private final AuthorizationSteps authorizationSteps = new AuthorizationSteps();
    private final VerificationSteps verificationSteps = new VerificationSteps();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.socket.timeout", 10000)
                );
        this.login = CourierTestData.generateUniqueLogin();
        this.password = CourierTestData.generateUniquePassword();
        this.firstName = CourierTestData.generateUniqueFirstName();

    }

    @AfterEach
    public void cleanUp() {
        if (createdCourierId != null) {
            courierSteps.deleteCourierById(createdCourierId);
        }
    }


    @Test
    @Description("Успешная авторизация курьера")
    public void shouldLoginCourierSuccessfully() {
        String createBody = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"firstName\":\"" + firstName + "\"}";
        String loginBody = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}";

        Response createResponse = courierSteps.createCourier(createBody);
        verificationSteps.verifyCourierCreatedSuccessfully(createResponse);

        Response authResponse = authorizationSteps.authorizationCourier(loginBody);
        verificationSteps.verifyAuthorizationSuccessful(authResponse);
        this.createdCourierId = authResponse.jsonPath().getInt("id");

    }


    @Test
    @Description("Авторизация без логина")
    public void shouldNotLoginCourierWithoutLogin() {
        String createBody = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"firstName\":\"" + firstName + "\"}";
        String loginBody = "{\"password\":\"" + password + "\"}";

        Response createResponse = courierSteps.createCourier(createBody);
        verificationSteps.verifyCourierCreatedSuccessfully(createResponse);
        Response authResponse = authorizationSteps.authorizationCourier(loginBody);
        verificationSteps.verifyError(authResponse, 400, "Недостаточно данных для входа");
    }

    @Test
    @Description("Авторизация без пароля")
    public void shouldNotLoginCourierWithoutPassword() {
        String createBody = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\",\"firstName\":\"" + firstName + "\"}";
        String loginBody = "{\"login\":\"" + login + "\"}";

        Response createResponse = courierSteps.createCourier(createBody);
        verificationSteps.verifyCourierCreatedSuccessfully(createResponse);
        Response authResponse = authorizationSteps.authorizationCourier(loginBody);
        verificationSteps.verifyError(authResponse, 400, "Недостаточно данных для входа");

    }

    @Test
    @Description("Авторизация несуществующим пользователем")
    public void shouldNotLoginWithNonExistentUser() {
        String loginBody = "{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}";
        Response authResponse = authorizationSteps.authorizationCourier(loginBody);
        verificationSteps.verifyError(authResponse, 404, "Учетная запись не найдена");

    }


}
