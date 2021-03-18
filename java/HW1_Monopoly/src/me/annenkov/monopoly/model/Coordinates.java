package me.annenkov.monopoly.model;

/**
 * Класс координат.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Coordinates {
    int X;
    int Y;

    public Coordinates(int x, int y) {
        X = x;
        Y = y;
    }

    /**
     * Метод получает пару координат X, Y по позиции.
     *
     * @param position Позиция игрока.
     * @param width    Ширина игрового поля.
     * @param height   Высота игрового поля.
     * @return Координаты игрока.
     */
    public static Coordinates getCoordinatesByPosition(int position, int width, int height) {
        if (position < width) {
            return new Coordinates(position, 0);
        } else if (position < width + height - 1) {
            return new Coordinates(width - 1, position - width + 1);
        } else if (position < 2 * width + height - 2) {
            return new Coordinates(width - 2 - (position - width - height + 1), height - 1);
        } else {
            return new Coordinates(0, width - (position - 2 * width - height + 4));
        }
    }

    /**
     * Метод возвращает строковое представление координат.
     *
     * @return Строковое представление.
     */
    @Override
    public String toString() {
        return "(" + (X + 1) + " , " + (Y + 1) + ")";
    }
}
