package me.annenkov.monopoly.model.player;

import me.annenkov.monopoly.ConsoleHelper;
import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.Coordinates;
import me.annenkov.monopoly.model.Cubes;

/**
 * Класс игрока.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Player {
    private final boolean isBot;

    private String name;
    private int money;
    private int position;
    private double spentOnShops;

    public Player(int money) {
        this(money, false);
    }

    public Player(int money, boolean isBot) {
        this.isBot = isBot;
        this.money = money;
        this.position = 0;
        this.spentOnShops = 0;
    }

    public boolean isBot() {
        return isBot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    /**
     * Тратит деньги с проверкой на неотрицательность.
     *
     * @param moneyToPay Количество денег, которое требуется заплатить.
     *                   Округляется математически.
     * @return Смог ли игрок заплатить деньги.
     */
    public boolean payMoney(double moneyToPay) {
        this.money -= Math.round(moneyToPay);
        return this.money >= 0;
    }

    /**
     * Зарабатывает деньги.
     *
     * @param moneyToEarn Количество денег, которое получает игрок.
     *                    Округляется математически.
     */
    public void earnMoney(double moneyToEarn) {
        this.money += Math.round(moneyToEarn);
    }

    /**
     * Получает координаты игрока в формате (X, Y)
     * по его позиции на поле.
     *
     * @param width  Ширина поля.
     * @param height Высота поля.
     * @return Координаты игрока.
     */
    public Coordinates getCoordinates(int width, int height) {
        return Coordinates.getCoordinatesByPosition(position, width, height);
    }

    public int getPosition() {
        return position;
    }

    /**
     * Перемещает игрока на cellsToAdd позиуий вперёд.
     * Учитывается зацикленность поля.
     *
     * @param cellsToAdd Количество клеток,
     *                   на которое игрока требуется переместить.
     * @param length     Длина поля.
     */
    public void addPosition(int cellsToAdd, int length) {
        this.position += cellsToAdd;
        this.position = this.position % length;
    }

    public double getSpentOnShops() {
        return spentOnShops;
    }

    public void addSpentOnShops(double moneyToAdd) {
        this.spentOnShops += moneyToAdd;
    }

    /**
     * Применяет действие к игроку.
     *
     * @param action Действие.
     * @param length Длина поля.
     * @return true — если действие выполнено успешно и игра продолжается, иначе false.
     */
    public boolean applyAction(Action action, int length) {
        switch (action.getType()) {
            case MOVE:
                addPosition((int) action.getValue(), length);
                break;


            case PAY_MONEY:
                return payMoney(action.getValue());


            case EARN_MONEY:
                earnMoney(action.getValue());
                break;


            case PAY_COMPENSATION:
                boolean isEnoughMoney = payMoney(action.getValue());
                if (isEnoughMoney)
                    action.getPlayerToPayCompensation().earnMoney(action.getValue());

                return isEnoughMoney;
        }

        return true;
    }

    /**
     * Метод для подбрасывания кубов.
     *
     * @return Сумма чисел на кубах.
     */
    public int throwCubes() {
        ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("throw_cubes") + '\n', name);

        Cubes cubes = new Cubes();
        ConsoleHelper.writeFormattedString(ConsoleHelper.getMessage("cube_numbers") + '\n', cubes.getCube1(),
                cubes.getCube2(), cubes.getSum());

        return cubes.getSum();
    }

    @Override
    public String toString() {
        return name;
    }
}
