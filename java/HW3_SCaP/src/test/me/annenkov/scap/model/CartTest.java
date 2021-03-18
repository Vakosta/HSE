package me.annenkov.scap.model;

import me.annenkov.scap.model.animals.Animal;
import me.annenkov.scap.model.animals.Swan;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CartTest {
    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @AfterEach
    void tearDown() {
        cart = null;
    }

    @Test
    void moveCart() {
        for (int i = 0; i < 200; i++) {
            double oldX = cart.getX();
            double oldY = cart.getY();

            Animal swan = new Swan(cart);
            cart.moveCart(swan);

            System.out.println(i);

            assertTrue(Math.abs(oldX - cart.getX()) < 10);
            assertTrue(Math.abs(oldY - cart.getY()) < 10);
        }
    }
}