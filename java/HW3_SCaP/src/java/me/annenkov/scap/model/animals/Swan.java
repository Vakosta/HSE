package me.annenkov.scap.model.animals;

import me.annenkov.scap.model.Cart;

public class Swan extends Animal {
    private static final int ANGLE = 60;

    public Swan(Cart cart) {
        super(cart, ANGLE);
    }

    public Swan(Cart cart, int wearinessReserve) {
        super(cart, ANGLE, wearinessReserve);
    }

    @Override
    public String toString() {
        return "Swan";
    }
}
