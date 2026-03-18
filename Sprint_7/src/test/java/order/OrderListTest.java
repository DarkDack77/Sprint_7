package order;

import base.BaseTest;
import client.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest {

    private OrderClient orderClient;

    @Before
    public void init() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что API возвращает список заказов")
    public void getOrdersListShouldReturnOrders() {
        orderClient.getOrdersList()
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}