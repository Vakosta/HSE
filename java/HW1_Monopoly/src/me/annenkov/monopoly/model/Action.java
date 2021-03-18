package me.annenkov.monopoly.model;

import me.annenkov.monopoly.model.player.Player;

/**
 * Класс действия.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Action {
    private final ActionType type;
    private final double value;
    private final Player playerToPayCompensation;

    public Action(ActionType type) {
        this(type, 0, null);
    }

    public Action(ActionType type, double value) {
        this(type, value, null);
    }

    public Action(ActionType type, double value, Player playerToPayCompensation) {
        this.type = type;
        this.value = value;
        this.playerToPayCompensation = playerToPayCompensation;
    }

    public double getValue() {
        return value;
    }

    public ActionType getType() {
        return type;
    }

    public Player getPlayerToPayCompensation() {
        return playerToPayCompensation;
    }

    /**
     * Тип действия.
     * На основе типа игрок будет решать, как ему действовать.
     */
    public enum ActionType {
        NOTHING,
        MOVE,
        PAY_MONEY,
        EARN_MONEY,
        PAY_COMPENSATION,
    }
}
