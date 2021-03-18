package me.annenkov.monopoly;

import me.annenkov.monopoly.model.ArgsCheckResult;

/**
 * Игра «Монополия».
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Monopoly {
    public static void main(String[] args) {
        ArgsCheckResult argsCheckResult = ArgsCheckResult.isArgsValid(args);

        switch (argsCheckResult.getType()) {

            case OK:
                Game game = new Game(Integer.parseInt(args[1]),
                        Integer.parseInt(args[0]),
                        Integer.parseInt(args[2]));
                game.run();
                break;


            case NUMBER_FORMAT_ERROR:
                ConsoleHelper.writeFormattedString("Неверные параметры при запуске. Введите числа!\n" +
                        "Обратите внимание на неверный параметр: \"%s\".", argsCheckResult.getValue());
                break;


            case QUANTITY_ERROR:
                ConsoleHelper.writeFormattedString("Не все параметры введены.");
                break;


            case WRONG_BORDERS:
                ConsoleHelper.writeFormattedString("Неверно заданы границы входных значений.");
                break;

        }
    }
}
