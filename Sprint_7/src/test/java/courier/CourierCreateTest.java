package courier;

import client.CourierClient;
import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import model.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest extends BaseTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void init() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
        courierId = null;
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            try {
                courierClient.deleteCourier(courierId);
            } catch (Exception e) {
                System.out.println("Не удалось удалить курьера: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка, что курьера можно успешно создать")
    public void createCourierShouldReturnOkTrue() {
        Response createResponse = courierClient.createCourier(courier);

        createResponse.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    @Description("Проверка, что нельзя создать курьера с уже существующим логином")
    public void createDuplicateCourierShouldReturnConflict() {
        courierClient.createCourier(courier);

        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.then().extract().path("id");

        Response duplicateResponse = courierClient.createCourier(courier);

        duplicateResponse.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка ошибки при создании курьера без логина")
    public void createCourierWithoutLoginShouldReturnBadRequest() {
        Courier courierWithoutLogin = new Courier(
                null,
                courier.getPassword(),
                courier.getFirstName()
        );

        Response response = courierClient.createCourier(courierWithoutLogin);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка ошибки при создании курьера без пароля")
    public void createCourierWithoutPasswordShouldReturnBadRequest() {
        Courier courierWithoutPassword = new Courier(
                courier.getLogin(),
                null,
                courier.getFirstName()
        );

        Response response = courierClient.createCourier(courierWithoutPassword);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}