package me.annenkov.monopoly.model;

import me.annenkov.monopoly.model.cell.*;
import me.annenkov.monopoly.model.player.Player;
import me.annenkov.monopoly.model.view.CellView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс игральной доски.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Board {
    private final int length;

    private final int width;
    private final int height;

    private final List<Cell> cells;

    /**
     * Создаётся поле. Автоматически инициализируется длина
     * и заполняются клетки.
     *
     * @param width  Ширина поля.
     * @param height Высота поля.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;

        this.length = 2 * (width - 2) + 2 * (height - 2) + 4;

        cells = new ArrayList<>();
        for (int i = 0; i < length; i++)
            cells.add(null);

        initCells();
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Cell> getCells() {
        return cells;
    }

    /**
     * Инициализирует клетки игральной доски.
     */
    private void initCells() {
        int quantity;
        if (Math.min(width, height) <= 7)
            quantity = ThreadLocalRandom.current().nextInt(0, 2);
        else
            quantity = ThreadLocalRandom.current().nextInt(0, 3);

        initEmptyCells();
        initCells(Cell.CellType.BANK, 1);
        initCells(Cell.CellType.TAXI, quantity);
        initCells(Cell.CellType.PENALTY_CELL, quantity);
        initShopCells();
    }

    /**
     * Инициализирует "пустые" угловые клетки.
     */
    private void initEmptyCells() {
        int firstAngleIndex = getEmptyCellIndex(EmptyCellNumber.EMPTY_CELL_FIRST);
        int secondAngleIndex = getEmptyCellIndex(EmptyCellNumber.EMPTY_CELL_SECOND);
        int thirdAngleIndex = getEmptyCellIndex(EmptyCellNumber.EMPTY_CELL_THIRD);
        int fourthAngleIndex = getEmptyCellIndex(EmptyCellNumber.EMPTY_CELL_FOURTH);

        cells.set(firstAngleIndex, new EmptyCell());
        cells.set(secondAngleIndex, new EmptyCell());
        cells.set(thirdAngleIndex, new EmptyCell());
        cells.set(fourthAngleIndex, new EmptyCell());
    }

    /**
     * Инициализирует клетки определённого типа.
     *
     * @param type     Тип клетки.
     * @param quantity Количество таких клеток на каждой линии.
     */
    private void initCells(Cell.CellType type, int quantity) {
        for (int i = 0; i < 4; i++) {
            int successGenerations = 0;

            while (successGenerations < quantity) {
                int startAngle = getEmptyCellIndex(EmptyCellNumber.getNameByCode(i));
                int endAngle;

                int index;
                if (i == 3) {
                    index = ThreadLocalRandom.current().nextInt(startAngle + 1, length);
                } else {
                    endAngle = getEmptyCellIndex(EmptyCellNumber.getNameByCode(i + 1));

                    index = ThreadLocalRandom.current().nextInt(startAngle + 1, endAngle);
                }

                if (cells.get(index) == null) {
                    initCellByType(type, index);
                    successGenerations++;
                }
            }
        }
    }

    /**
     * Инициализирует одну клетку определённого типа.
     *
     * @param type  Тип клетки.
     * @param index Индекс, по которому располагается клетка.
     */
    private void initCellByType(Cell.CellType type, int index) {
        switch (type) {
            case EMPTY_CELL:
                cells.set(index, new EmptyCell());
                break;
            case PENALTY_CELL:
                cells.set(index, new PenaltyCell(index + 1));
                break;
            case BANK:
                cells.set(index, new Bank(index + 1));
                break;
            case TAXI:
                cells.set(index, new Taxi(index + 1));
                break;
            case SHOP:
                cells.set(index, new Shop(index + 1));
                break;
        }
    }

    /**
     * Инициализирует клетки с магазинами вместо всех null-значений массива.
     */
    private void initShopCells() {
        for (int i = 0; i < length; i++) {
            if (cells.get(i) == null)
                cells.set(i, new Shop(i + 1));
        }
    }

    /**
     * Получает индекс угловой клетки по её номеру.
     *
     * @param number Номер угловой клетки, начиная от верхней левой.
     * @return Индекс угловой клетки в массиве.
     */
    private int getEmptyCellIndex(EmptyCellNumber number) {
        switch (number) {
            case EMPTY_CELL_FIRST:
                return 0;
            case EMPTY_CELL_SECOND:
                return width - 1;
            case EMPTY_CELL_THIRD:
                return width + height - 2;
            default:
                return 2 * width + height - 3;
        }
    }

    /**
     * Получает клетку по индексу.
     *
     * @param index Индекс клетки.
     * @return Клетка, расположенная на переданном индексе.
     */
    public Cell getCellByIndex(int index) {
        return cells.get(index % cells.size());
    }

    /**
     * Метод получает визуальное представление внешней клетки.
     * Используются дополнительные параметры для правильной отрисовки клеток.
     *
     * @param cell              Клетка, для которой требуется получить визуальное представление.
     * @param player            Игрок, который ходит в данный момент.
     *                          Используется для правильного отображения клеток магазинов.
     * @param isStartHorizontal Находится ли клетка слева.
     * @param isStartVertical   Находится ли клетка сверху.
     * @param isEndHorizontal   Находится ли клетка справа.
     * @param isEndVertical     Находится ли клетка снизу.
     * @return Визуальное представление клетки.
     */
    private CellView getExternalCellString(Cell cell,
                                           Player player,
                                           boolean isStartHorizontal,
                                           boolean isStartVertical,
                                           boolean isEndHorizontal,
                                           boolean isEndVertical) {
        CellView.Position[] positions = new CellView.Position[4];

        if (isStartHorizontal)
            positions[0] = CellView.Position.LEFT;
        if (isStartVertical)
            positions[1] = CellView.Position.TOP;
        if (isEndHorizontal)
            positions[2] = CellView.Position.RIGHT;
        if (isEndVertical)
            positions[3] = CellView.Position.BOTTOM;

        return new CellView(cell, player, positions);
    }

    /**
     * Метод получает визуальное представление внутренней клетки.
     *
     * @return Визуальное представление внутренней клетки.
     */
    private CellView getInternalCellString() {
        return new CellView();
    }

    /**
     * Добавляет к StringBuilder верхние клетки поля.
     *
     * @param stringBuilder Итоговый StringBuilder.
     * @param player        Игрок, который ходит в данный момент.
     *                      Используется для правильного отображения клеток магазинов.
     */
    private void addStartCellsToStringBuilder(StringBuilder stringBuilder, Player player) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < width; j++) {
                stringBuilder.append(getExternalCellString(
                        cells.get(j),
                        player,
                        j == 0,
                        true,
                        j == width - 1,
                        false
                ).getLines()[i]);
            }
            stringBuilder.append("\n");
        }
    }

    /**
     * Добавляет к StringBuilder средние клетки поля.
     *
     * @param stringBuilder Итоговый StringBuilder.
     * @param player        Игрок, который ходит в данный момент.
     *                      Используется для правильного отображения клеток магазинов.
     */
    private void addMiddleCellsToStringBuilder(StringBuilder stringBuilder, Player player) {
        for (int i = 0; i < height - 2; i++) {
            for (int k = 0; k < 2; k++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < width; j++) {
                    if (j == 0)
                        line.append(getExternalCellString(
                                cells.get(length - i - 1),
                                player,
                                true,
                                false,
                                false,
                                false
                        ).getLines()[k]);
                    else if (j == width - 1)
                        line.append(getExternalCellString(
                                cells.get(width + i),
                                player,
                                false,
                                false,
                                true,
                                false
                        ).getLines()[k]);
                    else
                        line.append(getInternalCellString().getLines()[k]);
                }
                stringBuilder.append(line).append("\n");
            }
        }
    }

    /**
     * Добавляет к StringBuilder нижние клетки поля.
     *
     * @param stringBuilder Итоговый StringBuilder.
     * @param player        Игрок, который ходит в данный момент.
     *                      Используется для правильного отображения клеток магазинов.
     */
    private void addEndCellsToStringBuilder(StringBuilder stringBuilder, Player player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < width; j++) {
                stringBuilder.append(getExternalCellString(
                        cells.get(length - (j + height - 1)),
                        player,
                        j == 0,
                        false,
                        j == width - 1,
                        true
                ).getLines()[i]);
            }
            stringBuilder.append("\n");
        }
    }

    /**
     * Метод для получения строкового представления доски.
     *
     * @param player Игрок, который ходит в данный момент.
     *               Используется для правильного отображения клеток магазинов.
     * @return Строковое представление доски.
     */
    public String toString(Player player) {
        StringBuilder result = new StringBuilder();

        addStartCellsToStringBuilder(result, player);
        addMiddleCellsToStringBuilder(result, player);
        addEndCellsToStringBuilder(result, player);

        return result.toString();
    }

    /**
     * Перечисление, которое определяет позиции угловых клеток.
     */
    enum EmptyCellNumber {
        EMPTY_CELL_FIRST(0),
        EMPTY_CELL_SECOND(1),
        EMPTY_CELL_THIRD(2),
        EMPTY_CELL_FOURTH(3);

        int code;

        EmptyCellNumber(int code) {
            this.code = code;
        }

        public static EmptyCellNumber getNameByCode(int code) {
            for (EmptyCellNumber e : EmptyCellNumber.values()) {
                if (code == e.code)
                    return e;
            }
            return null;
        }
    }
}
