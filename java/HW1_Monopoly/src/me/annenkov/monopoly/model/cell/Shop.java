package me.annenkov.monopoly.model.cell;

import me.annenkov.monopoly.ConsoleHelper;
import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.player.Player;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Shop extends Cell {
    private static final Cell.CellType TYPE = CellType.SHOP;

    private final double improvementCoeff;
    private final double compensationCoeff;

    private int cost;
    private int compensation;

    private Player owner;

    /**
     * Создаёт клетку магазина.
     *
     * @param number Номер клетки на поле.
     */
    public Shop(int number) {
        super(TYPE, number);

        improvementCoeff = ThreadLocalRandom.current().nextDouble(0.1, 2);
        compensationCoeff = ThreadLocalRandom.current().nextDouble(0.1, 1);

        cost = ThreadLocalRandom.current().nextInt(50, 500);
        compensation = ThreadLocalRandom.current().nextInt((int) Math.round(0.5 * cost), (int) Math.round(0.9 * cost));

        printInfo();
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * Само действие покупки или улучшения магазина.
     * Производятся необходимые вычисления:
     * устанавливается владелец, увеличивается цена, увеличивается компенсация.
     *
     * @param buyer Покупатель.
     * @return Цена покупки.
     */
    public int buyOrImprove(Player buyer) {
        if ((owner == null || owner == buyer) && buyer.getMoney() >= cost) {
            owner = buyer;

            int oldCost = cost;
            cost *= improvementCoeff;
            compensation *= compensationCoeff;

            return oldCost;
        }

        return -1;
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
        if (player.isBot() && (owner == null || owner == player)) {
            return getBotAction(player);
        }

        ConsoleHelper.writeStringLine("");
        if (owner == null) {
            return shopBuyOrImproveProcess(player, true);
        } else if (owner == player) {
            return shopBuyOrImproveProcess(player, false);
        } else {
            ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("someone_else_store") + '\n',
                    compensation);
            return new Action(Action.ActionType.PAY_COMPENSATION, compensation, owner);
        }
    }

    /**
     * Процесс покупки или улучшения магазина.
     *
     * @param player Игрок, который ходит.
     * @param isBuy  true — если магазин покупается, иначе false.
     * @return Действие для игрока.
     */
    private Action shopBuyOrImproveProcess(Player player, boolean isBuy) {
        if (isBuy)
            ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("shop_ask") + '\n', cost);
        else
            ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("shop_ask_improve") + '\n', cost);

        int answer;
        while (true) {
            answer = ConsoleHelper.readNumber();
            if (answer == 1 || answer == 2)
                break;

            ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("ask_repeat"));
        }

        if (answer == 1) {
            return buyOrImproveAccept(player, isBuy);
        }

        ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("shop_skip"));
        return new Action(Action.ActionType.NOTHING);
    }

    /**
     * Покупка или улучшение магазина, если игрок дал своё согласие.
     *
     * @param player Игрок, который ходит.
     * @param isBuy  true — если магазин покупается, иначе false.
     * @return Действие для игрока.
     */
    private Action buyOrImproveAccept(Player player, boolean isBuy) {
        int oldCost = buyOrImprove(player);
        if (oldCost != -1) {
            player.addSpentOnShops(oldCost);
            if (isBuy)
                ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("shop_bought"));
            else
                ConsoleHelper.writeStringLine(ConsoleHelper.getMessage("shop_improved"));
            return new Action(Action.ActionType.PAY_MONEY, oldCost);
        } else {
            if (isBuy)
                ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("shop_not_enough_money") +
                        '\n', cost - player.getMoney());
            else
                ConsoleHelper.writeFormattedString(ConsoleHelper
                        .getMessage("shop_improve_not_enough_money"), cost - player.getMoney());
            return new Action(Action.ActionType.NOTHING);
        }
    }

    /**
     * Получает действие для бота.
     *
     * @param player Игрок, который ходит.
     * @return Действие.
     */
    private Action getBotAction(Player player) {
        boolean isBuy = ThreadLocalRandom.current().nextBoolean();

        int oldCost = 0;
        if (isBuy)
            oldCost = buyOrImprove(player);

        if (oldCost != -1)
            return new Action(Action.ActionType.PAY_MONEY, oldCost);
        return new Action(Action.ActionType.NOTHING);
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
        return String.format("%s%" + (22 - (ConsoleHelper.getMessage("shop") + " —: " + getNumber()).length()) +
                        "s%27s %8.2f\n" +
                        "%22s%27s %8.2f\n" +
                        "%22s%27s %8d\n" +
                        "%22s%27s %8d",
                ConsoleHelper.getMessage("shop") + " — " + getNumber() + ":", "",
                ConsoleHelper.getMessage("improvement_coeff"), improvementCoeff,
                "", ConsoleHelper.getMessage("compensation_coeff"), compensationCoeff,
                "", ConsoleHelper.getMessage("cost"), cost,
                "", ConsoleHelper.getMessage("compensation"), compensation);
    }
}
