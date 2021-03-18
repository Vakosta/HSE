package me.annenkov.scap.display;

import me.annenkov.scap.ConsoleHelper;
import me.annenkov.scap.Parameters;
import me.annenkov.scap.model.Cart;
import me.annenkov.scap.model.animals.Animal;
import me.annenkov.scap.model.animals.Crayfish;
import me.annenkov.scap.model.animals.Pike;
import me.annenkov.scap.model.animals.Swan;

import java.util.List;

/**
 * Абстрактный класс для отрисовки данных программы.
 * <p>
 * Я использую отдельный класс для отрисовки данных с целью сделать архитектуру более гибкой.
 * Теперь, если потребуется реализовать графический интерфейс, достаточно унаследоваться от абстрактного класса
 * Display и реализовать метод displayChangedCoordinates().
 */
public abstract class Display implements Cart.OnCartUpdateListener {
    private double x;
    private double y;
    private Parameters.OutputMode outputMode;
    private List<Animal> animals;

    public Display() {
        this(0, 0, Parameters.OutputMode.STANDART);
    }

    public Display(double x, double y, Parameters.OutputMode outputMode) {
        this.x = x;
        this.y = y;
        this.outputMode = outputMode;
        onCreate();
    }

    /**
     * Создание класса на основе объекта Parameters, в котором хранится вся информация о переданных параметрах.
     *
     * @param parameters Объект с переданными параметрами.
     */
    public Display(Parameters parameters) {
        this(parameters.getX(), parameters.getY(), parameters.getOutputMode());
    }

    /**
     * Создание класса с возможностью отключить его работу в фоне.
     *
     * @param isWithoutRun Требуется ли выключить работу в фоне.
     */
    public Display(boolean isWithoutRun) {
        if (!isWithoutRun)
            onCreate();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Parameters.OutputMode getOutputMode() {
        return outputMode;
    }

    public synchronized List<Animal> getAnimals() {
        return animals;
    }

    /**
     * Метод получает все обновления координат x, y interface-слушателя
     * из объекта Cart.
     *
     * @param animal Животное, которое передвинуло тележку.
     * @param x      Актуальная координата x.
     * @param y      Актуальная координата y.
     */
    @Override
    public void onUpdate(Animal animal, double x, double y) {
        if (outputMode == Parameters.OutputMode.ALL_ACTIONS)
            ConsoleHelper.writeLine("[" + Thread.currentThread().getName() + "] " +
                    "Изменение координат: " + this.x + " " + this.y);

        this.x = x;
        this.y = y;

        if (outputMode == Parameters.OutputMode.ALL_ACTIONS) {
            ConsoleHelper.writeLine("Изменены координаты: " + this.x + " " + this.y);
            ConsoleHelper.writeLine("");
        }
    }

    /**
     * Метод возвращает объект Thread для обновления
     * координат для отображения каждые n секунд.
     *
     * @return Объект Thread.
     */
    private Thread getCoordinatesUpdateThread() {
        return new Thread(() -> {
            try {

                Thread.sleep(100);

                while (!Thread.currentThread().isInterrupted()) {
                    applyChangedCoordinates(x, y);
                    Thread.sleep(2000);
                }

            } catch (InterruptedException ignored) {
            }
        });
    }

    /**
     * Метод для инициализации работы "дисплея".
     */
    protected void onCreate() {
        Cart cart = new Cart(x, y);
        cart.addUpdateListener(this);

        Thread coordinatesUpdateThread = getCoordinatesUpdateThread();
        coordinatesUpdateThread.start();

        animals = List.of(new Swan(cart), new Crayfish(cart), new Pike(cart));
        animals.forEach(Thread::start);

        try {
            for (Animal animal : animals)
                animal.join();
        } catch (InterruptedException ignored) {
            animals.forEach(Thread::interrupt);
        }

        coordinatesUpdateThread.interrupt();
    }

    /**
     * Метод вызывается каждые 2 секунды.
     * В метод передаются актуальные координаты x, y.
     * Предполагаемое использование: отображение информации о координатах пользователю.
     *
     * @param x Актуальная координата x.
     * @param y Актуальная координата y.
     */
    protected abstract void applyChangedCoordinates(double x, double y);

    /**
     * Метод отображает итоговую информацию о координатах тележки.
     *
     * @param x Итоговая координата x.
     * @param y Итоговая координата y.
     */
    protected abstract void displayResult(double x, double y);
}
