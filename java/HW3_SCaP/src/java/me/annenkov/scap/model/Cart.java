package me.annenkov.scap.model;

import me.annenkov.scap.model.animals.Animal;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс телеги.
 */
public class Cart {
    private final List<OnCartUpdateListener> updateListeners;

    private double x;
    private double y;

    public Cart() {
        this.updateListeners = new ArrayList<>();
        this.x = 0;
        this.y = 0;
    }

    public Cart(double x, double y) {
        this.updateListeners = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Метод добавляет слушателя к телеге.
     *
     * @param updateListener Слушатель, которого требуется добавить.
     */
    public void addUpdateListener(OnCartUpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    /**
     * Метод передвигает телегу.
     * Объявлен synchronized, поэтому телегу не могут двигать два животных одновременно.
     *
     * @param animal Животное, которое двигает телегу.
     */
    public synchronized void moveCart(Animal animal) {
        x += animal.getCoefficient() * Math.cos(Math.toRadians(animal.getAngle()));
        y += animal.getCoefficient() * Math.sin(Math.toRadians(animal.getAngle()));

        // Сообщаем всем подписанным слушателям, что произошло движение телеги.
        updateListeners.forEach(updateListener -> updateListener.onUpdate(animal, x, y));
    }

    /**
     * Интерфейс-колбек для сообщений об изменениях в координатах.
     */
    public interface OnCartUpdateListener {
        void onUpdate(Animal animal, double x, double y);
    }
}
