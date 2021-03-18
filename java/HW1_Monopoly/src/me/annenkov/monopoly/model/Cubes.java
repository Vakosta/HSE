package me.annenkov.monopoly.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс игральных кубов.
 *
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class Cubes {
    private final int cube1;
    private final int cube2;

    /**
     * При создании объекта кубы "подбрасываются".
     */
    public Cubes() {
        this.cube1 = ThreadLocalRandom.current().nextInt(1, 7);
        this.cube2 = ThreadLocalRandom.current().nextInt(1, 7);
    }

    public int getCube1() {
        return cube1;
    }

    public int getCube2() {
        return cube2;
    }

    /**
     * Получает сумму чисел на кубах.
     *
     * @return Сумма чисел.
     */
    public int getSum() {
        return cube1 + cube2;
    }
}
