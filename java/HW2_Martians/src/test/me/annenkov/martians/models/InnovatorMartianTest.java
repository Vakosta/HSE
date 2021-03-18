package me.annenkov.martians.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InnovatorMartianTest {
    private final List<InnovatorMartian<Integer>> martians = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 6; i++)
            martians.add(new InnovatorMartian<>(Integer.class, i));
    }

    @Test
    void setGeneticCode() {
        assertEquals(1, 1);
    }

    @Test
    void setParent() {
        assertTrue(martians.get(0).setParent(martians.get(1)));
        assertTrue(martians.get(1).setParent(martians.get(2)));
        assertTrue(martians.get(2).setParent(martians.get(4)));
        assertTrue(martians.get(3).setParent(martians.get(4)));
        assertTrue(martians.get(4).setParent(martians.get(5)));
        assertFalse(martians.get(0).setParent(martians.get(5)));
        assertFalse(martians.get(5).setParent(martians.get(0)));
        assertFalse(martians.get(0).setParent(martians.get(1)));
        assertFalse(martians.get(0)
                .setParent(new ConservatorMartian<>(new InnovatorMartian<>(Integer.class, 999))));
    }

    @Test
    void setChildren() {
        assertTrue(martians.get(5).setChildren(new ArrayList<>() {{
            add(martians.get(4));
        }}));
        assertTrue(martians.get(4).setChildren(new ArrayList<>() {{
            add(martians.get(3));
            add(martians.get(2));
        }}));
        assertTrue(martians.get(2).setChildren(new ArrayList<>() {{
            add(martians.get(1));
        }}));
        assertTrue(martians.get(1).setChildren(new ArrayList<>() {{
            add(martians.get(0));
        }}));
        assertFalse(martians.get(1).setChildren(new ArrayList<>() {{
            add(martians.get(0));
            add(new ConservatorMartian<>(new InnovatorMartian<>(Integer.class, 999)));
        }}));
    }

    @Test
    void addChild() {
        assertTrue(martians.get(5).addChild(martians.get(4)));
        assertTrue(martians.get(4).addChild(martians.get(3)));
        assertTrue(martians.get(4).addChild(martians.get(2)));
        assertTrue(martians.get(2).addChild(martians.get(1)));
        assertTrue(martians.get(1).addChild(martians.get(0)));
        assertFalse(martians.get(1).addChild(martians.get(0)));
        assertFalse(martians.get(0).addChild(martians.get(5)));
        assertFalse(martians.get(0).addChild(martians.get(1)));
        assertFalse(martians.get(0).addChild(martians.get(0)));
        assertFalse(martians.get(0)
                .addChild(new ConservatorMartian<>(new InnovatorMartian<>(Integer.class, 999))));
    }

    @Test
    void removeChild() {
        martians.get(0).setParent(martians.get(1));
        martians.get(1).setParent(martians.get(2));
        martians.get(2).setParent(martians.get(4));
        martians.get(3).setParent(martians.get(4));
        martians.get(4).setParent(martians.get(5));

        assertFalse(martians.get(0).removeChild(martians.get(1)));
        assertTrue(martians.get(1).removeChild(martians.get(0)));
        assertFalse(martians.get(2).removeChild(martians.get(2)));
        assertEquals(martians.get(5).getDescendants().size(), 4);
    }
}