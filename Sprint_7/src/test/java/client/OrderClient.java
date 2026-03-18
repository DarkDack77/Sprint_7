package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(ORDER_PATH);
    }

    @Step("Получить список заказов")
    public Response getOrdersList() {
        return given()
                .get(ORDER_PATH);
    }
}