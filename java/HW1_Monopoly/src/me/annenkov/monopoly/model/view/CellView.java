package me.annenkov.monopoly.model.view;

import me.annenkov.monopoly.model.cell.Cell;
import me.annenkov.monopoly.model.cell.Shop;
import me.annenkov.monopoly.model.player.Player;

import java.util.Arrays;

/**
 * Класс визуального отображения клетки.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class CellView {
    private final String text;
    private final String[] lines;

    public CellView() {
        this(" ");
    }

    public CellView(String text) {
        this.text = "---\n|" + text + "|\n---";

        lines = new String[]{
                "----",
                "| " + text + " ",
                ""
        };
    }

    /**
     * Генерирует визуальное представление клетки по
     * заданным параметрам.
     *
     * @param cell      Клетка, которая будет отрисовываться.
     * @param player    Игрок, который ходит в текущий ход.
     * @param positions Параметры для задания позиции клетки.
     */
    public CellView(Cell cell, Player player, Position... positions) {
        String text = cell.getType().getSymbol();

        if (cell.getType() == Cell.CellType.SHOP
                && ((Shop) cell).getOwner() != null
                && ((Shop) cell).getOwner() == player)
            text = "M";
        else if (cell.getType() == Cell.CellType.SHOP
                && ((Shop) cell).getOwner() != null
                && ((Shop) cell).getOwner() != player)
            text = "O";

        this.text = "---\n|" + text + "|\n---";

        String endTopBottom = "";
        String endHorizontal = " ";
        if (Arrays.stream(positions).anyMatch(p -> p == Position.RIGHT)) {
            endHorizontal = " |";
            endTopBottom = "-";
        }

        String endVertical = "";
        if (Arrays.stream(positions).anyMatch(p -> p == Position.BOTTOM))
            endVertical = "----";

        lines = new String[]{
                "----" + endTopBottom,
                "| " + text + endHorizontal,
                endVertical + endTopBottom
        };
    }

    /**
     * Метод возвращает линии текстового представления клетки.
     *
     * @return Массив строковых линий.
     */
    public String[] getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * Позиции клетки.
     * Используются для правильной отрисовки.
     */
    public enum Position {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
    }
}
