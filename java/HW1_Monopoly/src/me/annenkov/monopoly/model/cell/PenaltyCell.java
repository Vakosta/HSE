package me.annenkov.monopoly.model.cell;

import me.annenkov.monopoly.ConsoleHelper;
import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.player.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class PenaltyCell extends Cell {
    private static final CellType TYPE = CellType.PENALTY_CELL;

    private final double penaltyCoeff;

    /**
     * Создаёт штрафную клетку.
     *
     * @param number Номер клетки на поле.
     */
    public PenaltyCell(int number) {
        super(TYPE, number);

        penaltyCoeff = ThreadLocalRandom.current().nextDouble(0.01, 0.1);

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
        return new Action(Action.ActionType.PAY_MONEY, penaltyCoeff * player.getMoney());
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
        NumberFormat formatter = new DecimalFormat("#0.00");
        return String.format("%s%" + (22 - (ConsoleHelper.getMessage("penalty") + " —: " + getNumber()).length()) +
                        "s%27s %8.2f",
                ConsoleHelper.getMessage("penalty") + " — " + getNumber() + ":", "",
                ConsoleHelper.getMessage("penalty_coeff"), penaltyCoeff);
    }
}
