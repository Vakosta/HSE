package me.annenkov.monopoly.model.cell;

import me.annenkov.monopoly.ConsoleHelper;
import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.player.Player;

/**
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */

public class EmptyCell extends Cell {
    private static final CellType TYPE = CellType.EMPTY_CELL;

    /**
     * Создаёт пустую клетку.
     */
    public EmptyCell() {
        super(TYPE, -1);
    }

    @Override
    public Action getAction(Player player) {
        return new Action(Action.ActionType.NOTHING);
    }

    @Override
    void printInfo() {
    }

    @Override
    public String toString() {
        return ConsoleHelper.getMessage("empty");
    }
}
