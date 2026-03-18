package order;

import base.BaseTest;
import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import model.OrderGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest extends BaseTest {

    private final List<String> color;
    private final OrderClient orderClient;

    public OrderCreateTest(List<String> color) {
        this.color = color;
        this.orderClient = new OrderClient();
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Collection<Object[]> getColorData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с разными вариантами цвета")
    public void createOrderShouldReturnTrack() {
        Order order = OrderGenerator.getOrder(color);

        orderClient.createOrder(order)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}