package Practikum;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import steps.OrderCreationSteps;
import steps.VerificationSteps;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class OrderCreationTest {
    private Integer TrackId;
    private final OrderCreationSteps orderCreationSteps = new OrderCreationSteps();
    private final VerificationSteps verificationSteps = new VerificationSteps();

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
        if (TrackId != null) {
            orderCreationSteps.cancelOrder(TrackId);
        }
    }

    // Читаем шаблон из файла
    private String readOrderTemplate() {
        try {
            return Files.readString(Paths.get("src/test/resources/order_template.txt"));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать файл шаблона", e);
        }
    }

    // Метод для предоставления тестовых данных
    static Stream<Arguments> orderDataProvider() {
        return Stream.of(
                // Формат: firstName, lastName, address, phone, comment, color, expectedStatusCode
                Arguments.of("Валерий", "Петров", "Москва", "89774583354", "Комментарий", "\"null\"", 201),          // успех
                Arguments.of("Валерий", "Петров", "Москва", "89774583354", "Комментарий", "\"BLACK\"", 201),         // успех
                Arguments.of("Сергей", "Иванов", "Город Кукушка", "89774583322", "null", "\"GREY\"", 201),           // успех
                Arguments.of("Анна", "Сидорова", "СПб", "+79991112233", "Срочно!", "\"BLACK\", \"GREY\"", 201),      // успех
                Arguments.of("", "", "", "", "", "", 400),                                                           // ошибка
                Arguments.of("Анна", "Сидорова", "СПб", "+79991112233", "Срочно!", "\"Yellow\", \"White\"", 400),    // ошибка
                Arguments.of("Сергей", "Иванов", "Город Кукушка", "89774583322", "null", "\"Yellow\"", 400)          // ошибка


        );
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    @Description("Создание заказа с различными данными")
    public void createOrderWithDifferentData(String firstName, String lastName, String address,
                                             String phone, String comment, String color, int expectedStatusCode) {

        String template = readOrderTemplate();
        String orderJson = String.format(template, firstName, lastName, address, phone, comment, color);

        Response response = orderCreationSteps.createOrder(orderJson);
        verificationSteps.verifyCreateOrder(response, expectedStatusCode);

        this.TrackId = response.jsonPath().getInt("track");

    }


    @Test
    @Description("Создание заказа с пустым телом") // проверить что приложение ждет хоть какое то тело
    public void shouldNotCreateOrderWithEmptyBody() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("")
                .log().all()
                .when()
                .post("/api/v1/orders")
                .then()
                .log().all()
                .statusCode(not(201))
                .body("track", not(notNullValue())).extract().response();

        this.TrackId = response.jsonPath().getInt("track");
    }


}

