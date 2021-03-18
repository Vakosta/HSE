package me.annenkov.scap.model.animals;

import me.annenkov.scap.model.Cart;

public class Crayfish extends Animal {
    private static final int ANGLE = 180;

    public Crayfish(Cart cart) {
        super(cart, ANGLE);
    }

    public Crayfish(Cart cart, int wearinessReserve) {
        super(cart, ANGLE, wearinessReserve);
    }

    @Override
    public String toString() {
        return "Crayfish";
    }
}
