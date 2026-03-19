package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .post(COURIER_PATH);
    }

    @Step("Авторизовать курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .post(LOGIN_PATH);
    }

    @Step("Удалить курьера по id: {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .delete(COURIER_PATH + "/" + courierId);
    }
}