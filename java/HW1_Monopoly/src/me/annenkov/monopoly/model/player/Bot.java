package me.annenkov.monopoly.model.player;

import me.annenkov.monopoly.ConsoleHelper;

/**
 * Класс игрока-бота.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Bot extends Player {
    /**
     * Создаётся игрок с именем "бот" и
     * включённым параметром isBot.
     *
     * @param money Количество денег игрока.
     */
    public Bot(int money) {
        super(money, true);

        setName(ConsoleHelper.getMessage("bot_name"));
    }
}
