package me.annenkov.monopoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Класс с вспомогательными инструментами для работы с консолью.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class ConsoleHelper {
    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    /**
     * Метод устанавливает локализацию для сообщений консоли.
     *
     * @param locale Локализация для сообщений.
     */
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Метод возвращает сообщение по ключу с учётом локализации.
     *
     * @param key Ключ сообщения.
     * @return Значение сообщения по переданному ключу.
     */
    public static String getMessage(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException exception) {
            return "UNKNOWN KEY!";
        }
    }

    /**
     * Метод читает строку из консоли до перехода на следующую строку.
     *
     * @return Строка, считанная из консоли.
     */
    public static String readString() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                return bufferedReader.readLine();
            } catch (IOException e) {
                writeStringLine("Пожалуйста, введите корректный текст.");
            }
        }
    }

    /**
     * Метод читает число из консоли и возвращает его.
     *
     * @return Число, считанное из консоли.
     */
    public static int readNumber() {
        while (true) {
            try {
                return Integer.parseInt(readString());
            } catch (NumberFormatException e) {
                writeStringLine("Пожалуйста, введите корректное число.");
            }
        }
    }

    /**
     * Метод печатает текст в консоль.
     *
     * @param args Текст, который нужно напечатать в консоль.
     */
    public static void writeFormattedString(Object... args) {
        Object[] params = Arrays.copyOfRange(args, 1, args.length);
        System.out.printf(args[0] + "", params);
    }

    /**
     * Метод печатает текст в консоль.
     *
     * @param text Текст, который нужно напечатать в консоль.
     */
    public static void writeString(String text) {
        System.out.print(text);
    }

    /**
     * Метод печатает текст в консоль с переносом на новую строку.
     *
     * @param text Текст, который нужно напечатать в консоль.
     */
    public static void writeStringLine(String text) {
        System.out.print(text);
        System.out.println();
    }

    /**
     * Метод очищает консоль.
     */
    public static void clearConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}
