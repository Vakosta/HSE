package me.annenkov.scap.display;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisplayTest {
    private Display display;

    @BeforeEach
    void setUp() {
        display = new ConsoleDisplay(true);
    }

    @AfterEach
    void tearDown() {
        display = null;
    }

    @Test
    void onUpdate() {
        for (int i = 0; i < 1000; i++) {
            double x = ThreadLocalRandom.current().nextDouble(-1000, 1000);
            double y = ThreadLocalRandom.current().nextDouble(-1000, 1000);

            display.onUpdate(null, x, y);

            assertEquals(x, display.getX());
            assertEquals(y, display.getY());
        }
    }
}