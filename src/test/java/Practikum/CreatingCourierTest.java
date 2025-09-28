package Practikum;

import io.qameta.allure.Description;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import steps.CourierSteps;
import steps.VerificationSteps;
import testClass.CourierTestData;


public class CreatingCourierTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private final VerificationSteps verificationSteps = new VerificationSteps();
    private String createdCourierLogin;
    private String createdCourierPassword;

    static {
        RestAssured.filters(new AllureRestAssured());
    }
    @BeforeEach
    public void setUp() {

        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.socket.timeout", 10000)
                );
    }

    @AfterEach
    public void cleanUp() {
        if (createdCourierLogin != null && createdCourierPassword != null) {
            courierSteps.deleteCourierByLogin(createdCourierLogin, createdCourierPassword);
        }
    }

    @Test
    @Description("Успешное создание курьера")
    public void shouldCreateCourierSuccessfully() {
        Response response = courierSteps.createCourier();
        verificationSteps.verifyCourierCreatedSuccessfully(response);

        this.createdCourierLogin = CourierTestData.getLastLogin();
        this.createdCourierPassword = CourierTestData.getLastPassword();
    }

    /**
     * Создание курьера без имени
     * Примечание: используется успешный код ответа, так как в документации
     * не указана обязательность полей явно, но по описанию ошибки API
     * можно предположить, что поле необязательно.
     */
    @Test
    @Description("Создание курьера без имени")
    public void shouldCreateCourierWithoutFirstName() {
        Response response = courierSteps.createCourierWithoutFirstName();
        verificationSteps.verifyCourierCreatedSuccessfully(response);

        this.createdCourierLogin = CourierTestData.getLastLogin();
        this.createdCourierPassword = CourierTestData.getLastPassword();
    }

    @Test
    @Description("Создание курьера без логина")
    public void shouldNotCreateCourierWithoutLogin() {
        Response response = courierSteps.createCourierWithoutLogin();
        verificationSteps.verifyError(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @Description("Создание курьера без пароля")
    public void shouldNotCreateCourierWithoutPassword() {
        Response response = courierSteps.createCourierWithoutPassword();
        verificationSteps.verifyError(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @Description("Создание курьера с уже существующим логином")
    public void shouldNotCreateDuplicateCourier() {
        Response response = courierSteps.createDuplicateCourier();
        verificationSteps.verifyError(response, 409, "логин уже используется");

        this.createdCourierLogin = CourierTestData.getLastLogin();
        this.createdCourierPassword = CourierTestData.getLastPassword();
    }


}
