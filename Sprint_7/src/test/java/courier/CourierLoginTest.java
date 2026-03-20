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

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
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
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин без логина")
    @Description("Проверка ошибки при отсутствии логина")
    public void loginWithoutLoginShouldReturnBadRequest() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());

        courierClient.loginCourier(credentials)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Неверный логин")
    @Description("Проверка ошибки при неверном логине")
    public void loginWithWrongLoginShouldReturnNotFound() {
        CourierCredentials wrongLogin =
                new CourierCredentials("wrongLogin", courier.getPassword());

        courierClient.loginCourier(wrongLogin)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Неверный пароль")
    @Description("Проверка ошибки при неверном пароле")
    public void loginWithWrongPasswordShouldReturnNotFound() {
        CourierCredentials wrongPassword =
                new CourierCredentials(courier.getLogin(), "wrongPassword");

        courierClient.loginCourier(wrongPassword)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Несуществующий пользователь")
    @Description("Проверка ошибки при авторизации несуществующего пользователя")
    public void loginWithNonExistentCourierShouldReturnNotFound() {
        CourierCredentials nonExistentCourier =
                new CourierCredentials("nonExistentLogin", "nonExistentPassword");

        courierClient.loginCourier(nonExistentCourier)
                .then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}