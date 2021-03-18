package me.annenkov.scap;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ConsoleHelper {
    /**
     * Метод печатает текущий прогресс перетягивания телеги.
     *
     * @param x         Актуальная координата x.
     * @param y         Актуальная координата y.
     * @param timeToEnd Время до конца перетягивания.
     */
    public static void printProgress(double x, double y, double timeToEnd) {
        ConsoleHelper.clearConsole();

        ConsoleHelper.writeLine("Текущий статус перетягивания:");

        ConsoleHelper.printCoordinatesString(x, y);
        ConsoleHelper.printTimeToEndString(timeToEnd);
    }

    /**
     * Метод печатает итоговый статус перетягивания телеги.
     *
     * @param x Итоговая координата x.
     * @param y Итоговая координата y.
     */
    public static void printResult(double x, double y) {
        ConsoleHelper.clearConsole();

        ConsoleHelper.writeLine("Итоговый статус перетягивания:");

        ConsoleHelper.printCoordinatesString(x, y);
        printCat();
    }

    /**
     * Метод печатает координаты в консоль.
     *
     * @param x        Координата x.
     * @param y        Координата y.
     * @param isResult Является ли результат конечным.
     */
    public static void printCoordinatesString(double x, double y, boolean isResult) {
        NumberFormat formatter = new DecimalFormat("#0.00");

        if (!isResult)
            write("Координаты: ");
        else
            write("Итоговые координаты: ");

        writeLine("[ " + formatter.format(x) + " ; " + formatter.format(y) + " ]");
    }

    /**
     * Метод печатает координаты в консоль.
     *
     * @param x Координата x.
     * @param y Координата y.
     */
    public static void printCoordinatesString(double x, double y) {
        printCoordinatesString(x, y, false);
    }

    /**
     * Метод печатает время до конца работы в консоль.
     *
     * @param timeToEnd Время до конца работы.
     */
    public static void printTimeToEndString(double timeToEnd) {
        NumberFormat formatter = new DecimalFormat("#0.0");
        writeLine("Время до конца: " + formatter.format(timeToEnd));
    }

    /**
     * Метод печатает строку в консоль.
     *
     * @param text Строка, которую требуется напечатать.
     */
    public static void write(String text) {
        System.out.print(text);
    }

    /**
     * Метод печатает строку в консоль с переходом на новую строку.
     *
     * @param text Строка, которую требуется напечатать.
     */
    public static void writeLine(String text) {
        write(text + '\n');
    }

    /**
     * Метод очищает консоль.
     */
    public static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    /**
     * Метод рисует кота в консоль.
     */
    public static void printCat() {
        System.out.println("   /\\_/\\   ");
        System.out.println("  / o o \\  ");
        System.out.println(" (   \"   ) ");
        System.out.println("  \\~(*)~/  ");
        System.out.println("   // \\\\   ");
    }
}
