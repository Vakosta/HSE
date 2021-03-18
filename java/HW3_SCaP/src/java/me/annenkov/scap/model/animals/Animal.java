package me.annenkov.scap.model.animals;

import me.annenkov.scap.model.Cart;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс абстрактного животного.
 */
public abstract class Animal extends Thread {
    public static final int DEFAULT_WEARINESS_RESERVE = 25000;
    public static final int LEFT_BOUND_COEFFICIENT = 1;
    public static final int RIGHT_BOUND_COEFFICIENT = 10;
    private static final int LEFT_BOUND_SLEEP_TIME = 1000;
    private static final int RIGHT_BOUND_SLEEP_TIME = 5000;

    protected final double angle;
    protected final double coefficient;

    private final Cart cart;
    private final long timeOfStart;
    private final long wearinessReserve;

    public Animal(Cart cart, double angle) {
        this(cart, angle, DEFAULT_WEARINESS_RESERVE);
    }

    public Animal(Cart cart, double angle, int wearinessReserve) {
        this.setName(toString());
        this.cart = cart;

        this.angle = angle;
        this.coefficient = getRandomCoefficient();

        this.timeOfStart = System.currentTimeMillis();
        this.wearinessReserve = wearinessReserve;
    }

    private static double getRandomCoefficient() {
        return ThreadLocalRandom.current().nextDouble(LEFT_BOUND_COEFFICIENT, RIGHT_BOUND_COEFFICIENT);
    }

    public double getAngle() {
        return angle;
    }

    public double getCoefficient() {
        return coefficient;
    }

    /**
     * Метод возвращает количество миллисекунд до конца работы животного.
     *
     * @return Количество миллисекунд до конца работы животного.
     */
    public long getMillisecondsToEnd() {
        return timeOfStart + wearinessReserve - System.currentTimeMillis();
    }

    /**
     * Метод заставляет животное подвинуть тележку.
     */
    private void pullCart() {
        cart.moveCart(this);
    }

    /**
     * Метод возвращает объект Thread, который отсчитывает время до конца работы животного.
     * В самом конце метод прерывает Thread самого животного.
     *
     * @return Объект-таймер.
     */
    private Thread getTimerToWearinessThread() {
        return new Thread(() -> {
            try {
                Thread.sleep(wearinessReserve);
            } catch (InterruptedException ignored) {
            }

            interrupt();
        });
    }

    /**
     * Основной поток работы животного.
     */
    @Override
    public void run() {
        try {

            // Таймер останавливает работу животного спустя какое-то количество секунд.
            getTimerToWearinessThread().start();

            // В цикле животное передвигает тележку до тех пор, пока окончательно не устанет.
            while (!isInterrupted()) {
                pullCart();
                Thread.sleep(ThreadLocalRandom.current().nextInt(LEFT_BOUND_SLEEP_TIME, RIGHT_BOUND_SLEEP_TIME));
            }

        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public String toString() {
        return "Animal";
    }
}