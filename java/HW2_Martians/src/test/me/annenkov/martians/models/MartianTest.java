package me.annenkov.martians.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MartianTest {
    private final List<InnovatorMartian<Integer>> martians = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 6; i++)
            martians.add(new InnovatorMartian<>(Integer.class, i));

        martians.get(0).setParent(martians.get(1));
        martians.get(1).setParent(martians.get(2));
        martians.get(2).setParent(martians.get(4));
        martians.get(3).setParent(martians.get(4));
        martians.get(4).setParent(martians.get(5));
    }

    @Test
    void getParent() {
        assertEquals(martians.get(1), martians.get(0).getParent());
        assertEquals(martians.get(2), martians.get(1).getParent());
        assertEquals(martians.get(4), martians.get(2).getParent());
        assertEquals(martians.get(4), martians.get(3).getParent());
        assertEquals(martians.get(5), martians.get(4).getParent());
        assertNull(martians.get(5).getParent());
    }

    @Test
    void getChildren() {
        assertEquals(0, martians.get(0).getChildren().size());
        assertEquals(1, martians.get(1).getChildren().size());
        assertEquals(1, martians.get(2).getChildren().size());
        assertEquals(0, martians.get(3).getChildren().size());
        assertEquals(2, martians.get(4).getChildren().size());
        assertEquals(1, martians.get(5).getChildren().size());
    }

    @Test
    void getDescendants() {
        assertEquals(0, martians.get(0).getDescendants().size());
        assertEquals(1, martians.get(1).getDescendants().size());
        assertEquals(2, martians.get(2).getDescendants().size());
        assertEquals(0, martians.get(3).getDescendants().size());
        assertEquals(4, martians.get(4).getDescendants().size());
        assertEquals(5, martians.get(5).getDescendants().size());
    }

    @Test
    void hasChildWithValue() {
        assertFalse(martians.get(0).hasChildWithValue(1));
        assertFalse(martians.get(1).hasChildWithValue(1));
        assertTrue(martians.get(2).hasChildWithValue(1));
        assertFalse(martians.get(3).hasChildWithValue(4));
        assertTrue(martians.get(4).hasChildWithValue(3));
        assertTrue(martians.get(5).hasChildWithValue(4));
    }

    @Test
    void hasDescendantWithValue() {
        assertFalse(martians.get(0).hasDescendantWithValue(1));
        assertFalse(martians.get(1).hasDescendantWithValue(1));
        assertTrue(martians.get(2).hasDescendantWithValue(0));
        assertFalse(martians.get(3).hasDescendantWithValue(0));
        assertTrue(martians.get(4).hasDescendantWithValue(1));
        assertTrue(martians.get(5).hasDescendantWithValue(0));
    }
}