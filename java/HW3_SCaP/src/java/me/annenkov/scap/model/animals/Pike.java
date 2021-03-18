package me.annenkov.scap.model.animals;

import me.annenkov.scap.model.Cart;

public class Pike extends Animal {
    private static final int ANGLE = 300;

    public Pike(Cart cart) {
        super(cart, ANGLE);
    }

    public Pike(Cart cart, int wearinessReserve) {
        super(cart, ANGLE, wearinessReserve);
    }

    @Override
    public String toString() {
        return "Pike";
    }
}
