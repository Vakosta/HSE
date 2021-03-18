package me.annenkov.scap.display;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class ConsoleDisplayTest {
    @Test
    void onCreate() {
        assertTimeoutPreemptively(Duration.ofSeconds(26), (ThrowingSupplier<ConsoleDisplay>) ConsoleDisplay::new);
    }
}