package me.annenkov.monopoly.model.cell;

import me.annenkov.monopoly.model.Action;
import me.annenkov.monopoly.model.player.Player;

/**
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public abstract class Cell {
    private final CellType type;
    private final int number;

    public Cell(CellType type, int number) {
        this.type = type;
        this.number = number;
    }

    public CellType getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Получает действие клетки.
     * На основе действия игрок будет делать ход.
     *
     * @param player Игрок на текущей клетке.
     *               Используется для правильного подсчёта коэффициентов.
     * @return Действие.
     */
    public abstract Action getAction(Player player);

    /**
     * Метод печатает информацию о клетке.
     */
    abstract void printInfo();

    /**
     * Метод возвращает информацию о клетке в виде строки.
     *
     * @return Строка с информацией.
     */
    @Override
    public abstract String toString();

    /**
     * Тип клетки. Учитывается при отрисовке поля в консоли.
     */
    public enum CellType {
        EMPTY_CELL("E"),
        PENALTY_CELL("%"),
        BANK("$"),
        TAXI("T"),
        SHOP("S");

        private final String symbol;

        CellType(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
