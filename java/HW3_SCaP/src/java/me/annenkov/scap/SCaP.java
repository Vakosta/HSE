package me.annenkov.scap;

import me.annenkov.scap.display.ConsoleDisplay;

/**
 * Класс запуска задачи «Лебедь, рак и щука».
 * Надеюсь, мой код будет приятно читать. :)
 * <p>
 * Хочу отметить, что вывод в формате "standart" лично у меня красивее выглядит при запуске из консоли.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class SCaP {
    public static void main(String[] args) {
        try {

            Parameters parameters = new Parameters(args);
            new ConsoleDisplay(parameters);

        } catch (Exception ex) {
            ConsoleHelper.writeLine("Вы где-то ошиблись в вводе параметров. Попробуйте ещё раз. :)");
        }
    }
}
