package me.annenkov.monopoly.model.cell;

import me.annenkov.monopoly.ConsoleHelper;
import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Bank extends Cell {
    private static final CellType TYPE = CellType.BANK;
    private static final Map<Player, Integer> debtors = new HashMap<>();
    private final double creditCoeff;
    private final double debtCoeff;

    /**
     * Создаёт клетку банка.
     *
     * @param number Номер клетки на поле.
     */
    public Bank(int number) {
        super(TYPE, number);

        creditCoeff = ThreadLocalRandom.current().nextDouble(0.002, 0.2);
        debtCoeff = ThreadLocalRandom.current().nextDouble(1, 3);

        printInfo();
    }

    /**
     * Метод возвращает действие для получения кредита.
     *
     * @param player Игрок, который будет получать кредит.
     * @return Действие для получения кредита.
     */
    private Action getCreditAction(Player player) {
        double maxCredit = creditCoeff * player.getSpentOnShops();
        ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("input_credit") + '\n', maxCredit);

        int creditSum;
        while (true) {
            creditSum = ConsoleHelper.readNumber();
            if (creditSum >= 0 && creditSum <= maxCredit)
                break;

            ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("input_credit_repeat") + ' ', maxCredit);
        }

        if (creditSum == 0) {
            ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("without_credit"));
            return new Action(Action.ActionType.NOTHING);
        }

        debtors.put(player, creditSum);
        ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("credit_got"));
        return new Action(Action.ActionType.EARN_MONEY, creditSum);
    }

    /**
     * Метод возвращает действие для игрока, которого нет
     * в списке должников.
     *
     * @param player Игрок, которого нет в списке должников.
     * @return Итоговое действие.
     */
    private Action playerNotInDebtorsProcess(Player player) {
        ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("credit_ask"));

        int answer;
        while (true) {
            answer = ConsoleHelper.readNumber();
            if (answer == 1 || answer == 2)
                break;

            ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("ask_repeat"));
        }

        switch (answer) {
            case 1:
                return getCreditAction(player);

            case 2:
                ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("without_credit"));
                return new Action(Action.ActionType.NOTHING);
        }

        return new Action(Action.ActionType.NOTHING);
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
        if (player.isBot())
            return new Action(Action.ActionType.NOTHING);

        if (!debtors.containsKey(player)) {
            return playerNotInDebtorsProcess(player);
        }

        double debtSum = debtCoeff * debtors.get(player);
        return new Action(Action.ActionType.PAY_MONEY, debtSum);
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
        return String.format("%s%" + (22 - (ConsoleHelper.getMessage("bank") + " —: " + getNumber()).length()) +
                        "s%27s %8.2f\n" +
                        "%22s%27s %8.2f",
                ConsoleHelper.getMessage("bank") + " — " + getNumber() + ":", "",
                ConsoleHelper.getMessage("credit_coeff"), creditCoeff,
                "", ConsoleHelper.getMessage("debt_coeff"), debtCoeff);
    }
}
