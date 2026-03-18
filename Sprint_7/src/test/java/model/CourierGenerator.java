package model;

import java.util.UUID;

public class CourierGenerator {

    public static Courier getRandomCourier() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new Courier(
                "courier" + unique,
                "1234",
                "Name" + unique
        );
    }
}