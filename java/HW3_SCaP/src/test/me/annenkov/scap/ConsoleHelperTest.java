package me.annenkov.scap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleHelperTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void printProgress() {
        ConsoleHelper.printProgress(123, 321, 100);
        assertEquals(
                "\n".repeat(50) +
                        "Текущий статус перетягивания:\n" +
                        "Координаты: [ 123,00 ; 321,00 ]\n" +
                        "Время до конца: 100,0\n",
                outContent.toString()
        );
    }

    @Test
    void printTimeToEndString() {
        ConsoleHelper.printTimeToEndString(123);
        assertEquals(
                "Время до конца: 123,0\n",
                outContent.toString()
        );
    }

    @Test
    void printCoordinatesString() {
        ConsoleHelper.printCoordinatesString(200, 100);
        assertEquals(
                "Координаты: [ 200,00 ; 100,00 ]\n",
                outContent.toString()
        );
    }

    @Test
    void printCoordinatesStringResult() {
        ConsoleHelper.printCoordinatesString(200, 100, true);
        assertEquals(
                "Итоговые координаты: [ 200,00 ; 100,00 ]\n",
                outContent.toString()
        );
    }

    @Test
    void write() {
        String s = "KekCheburek";
        ConsoleHelper.write(s);
        assertEquals(s, outContent.toString());
    }

    @Test
    void writeLine() {
        String s = "KekCheburek";
        ConsoleHelper.writeLine(s);
        assertEquals(s + '\n', outContent.toString());
    }

    @Test
    void clearConsole() {
        ConsoleHelper.clearConsole();
        assertEquals("\n".repeat(50), outContent.toString());
    }
}