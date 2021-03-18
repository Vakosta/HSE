package me.annenkov.martians.models;

import org.junit.jupiter.api.Test;

class ConservatorMartianTest {
    private final Martian<Integer> martian = new ConservatorMartian<>(
            new InnovatorMartian<>(Integer.class, 123)
    );

    @Test
    void getParent() {
    }

    @Test
    void getChildren() {
    }
}