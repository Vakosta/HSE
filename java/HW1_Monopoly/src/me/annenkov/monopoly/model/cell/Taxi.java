package me.annenkov.monopoly.model.cell;

import me.annenkov.monopoly.ConsoleHelper;
import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.player.Player;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */

public class Taxi extends Cell {
    private static final CellType TYPE = CellType.TAXI;

    private int taxiDistance;

    /**
     * Создаёт клетку такси.
     *
     * @param number Номер клетки на поле.
     */
    public Taxi(int number) {
        super(TYPE, number);

        printInfo();
    }

    /**
     * Получает действие клетки.
     * На основе действия игрок будет делать ход.
     *
     * @param player Игрок на текущей клетке.
     *               Используется для правильного подсчёта коэффициентов.
     * @return Действие.
     */
    @Override
    public Action getAction(Player player) {
        taxiDistance = ThreadLocalRandom.current().nextInt(3, 6);

        ConsoleHelper.writeStringLine("");
        for (int i = 0; i < 8; i++) {
            ConsoleHelper.writeString(">");

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Nothing
            }
        }
        ConsoleHelper.writeFormattedString(" " + ConsoleHelper.getMessage("taxi_cells") + " ",
                taxiDistance);
        for (int i = 0; i < 8; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // Nothing
            }

            ConsoleHelper.writeString(">");
        }
        ConsoleHelper.writeStringLine("");
        ConsoleHelper.writeStringLine("");

        return new Action(Action.ActionType.MOVE, taxiDistance);
    }

    /**
     * Метод печатает информацию о клетке.
     */
    @Override
    void printInfo() {
        ConsoleHelper.writeStringLine(toString() + "\n");
    }

    /**
     * Метод возвращает информацию о клетке в виде строки.
     *
     * @return Строка с информацией.
     */
    @Override
    public String toString() {
        return String.format("%s",
                ConsoleHelper.getMessage("taxi") + " — " + getNumber());
    }
}
