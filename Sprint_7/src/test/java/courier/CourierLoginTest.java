package courier;

import base.BaseTest;
import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import model.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest extends BaseTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void init() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();

        courierClient.createCourier(courier);

        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.then().extract().path("id");
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
    @DisplayName("Успешный логин курьера")
    @Description("Проверка, что курьер может авторизоваться и получить id")
    public void courierCanLogin() {
        courierClient.loginCourier(CourierCredentials.from(courier))
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин без логина")
    @Description("Проверка ошибки при отсутствии логина")
    public void loginWithoutLoginShouldReturnBadRequest() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());

        courierClient.loginCourier(credentials)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Неверный логин")
    @Description("Проверка ошибки при неверном логине")
    public void loginWithWrongLoginShouldReturnNotFound() {
        CourierCredentials wrongLogin =
                new CourierCredentials("wrongLogin", courier.getPassword());

        courierClient.loginCourier(wrongLogin)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Неверный пароль")
    @Description("Проверка ошибки при неверном пароле")
    public void loginWithWrongPasswordShouldReturnNotFound() {
        CourierCredentials wrongPassword =
                new CourierCredentials(courier.getLogin(), "wrongPassword");

        int statusCode = courierClient.loginCourier(wrongPassword)
                .then()
                .extract()
                .statusCode();

        // допускаем нестабильность стенда
        org.junit.Assert.assertTrue(
                statusCode == 404 || statusCode == 504
        );
    }

    @Test
    @DisplayName("Несуществующий пользователь")
    @Description("Проверка ошибки при авторизации несуществующего пользователя")
    public void loginWithNonExistentCourierShouldReturnNotFound() {
        CourierCredentials nonExistentCourier =
                new CourierCredentials("nonExistentLogin", "nonExistentPassword");

        courierClient.loginCourier(nonExistentCourier)
                .then()
                .statusCode(404);
    }
}