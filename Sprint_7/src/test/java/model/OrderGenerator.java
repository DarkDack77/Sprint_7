package model;

import java.util.List;

public class OrderGenerator {

    public static Order getOrder(List<String> color) {
        return new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2026-06-06",
                "Saske, come back to Konoha",
                color
        );
    }
}