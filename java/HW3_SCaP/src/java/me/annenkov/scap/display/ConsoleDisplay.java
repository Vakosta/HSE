package me.annenkov.scap.display;

import me.annenkov.scap.ConsoleHelper;
import me.annenkov.scap.Parameters;
import me.annenkov.scap.model.animals.Animal;

/**
 * Класс для отрисовки данных программы.
 * <p>
 * Я использую отдельный класс для отрисовки данных с целью сделать архитектуру более гибкой.
 * Теперь, если потребуется реализовать графический интерфейс, достаточно унаследоваться от абстрактного класса
 * Display и поменять требуемые методы.
 */
public class ConsoleDisplay extends Display {
    private double updatedX, updatedY;

    public ConsoleDisplay() {
    }

    /**
     * Создание класса на основе объекта Parameters, в котором хранится вся информация о переданных параметрах.
     *
     * @param parameters Объект с переданными параметрами.
     */
    public ConsoleDisplay(Parameters parameters) {
        super(parameters);
    }

    /**
     * Создание класса с возможностью отключить его работу в фоне.
     *
     * @param isWithoutRun Требуется ли выключить работу в фоне.
     */
    public ConsoleDisplay(boolean isWithoutRun) {
        super(isWithoutRun);
    }

    /**
     * Метод возвращает время до конца работы всех потоков.
     * Считается по времени потока, который закончит свою работу позже всех.
     *
     * @return Время до конца работы.
     */
    private double getTimeToEnd() {
        return getAnimals().stream()
                .filter(animal -> !animal.isInterrupted()) // Берём всех не прерванных животных.
                .mapToDouble(Animal::getMillisecondsToEnd) // Берём их время до конца.
                .max().orElse(0) / 1000; // Получаем среднее в секундах.
    }

    /**
     * Метод возвращает объект Thread для обновления консоли каждые n миллисекунд.
     * Сделано для того, чтобы в консоли обновлялось время до конца быстрее, чем обновляются координаты.
     * По условию координаты должны обновляться каждые 2 секунды.
     *
     * @return Объект Thread.
     */
    private Thread getUpdateConsoleThread() {
        return new Thread(() -> {
            try {

                // Цикл ожидает заполнения животных.
                while (getAnimals() == null || getAnimals().isEmpty())
                    Thread.sleep(100);

                while (!Thread.currentThread().isInterrupted()) {
                    if (getOutputMode() == Parameters.OutputMode.STANDART)
                        ConsoleHelper.printProgress(updatedX, updatedY, getTimeToEnd());
                    Thread.sleep(150);
                }

            } catch (InterruptedException ignored) {
            }
        });
    }

    /**
     * Метод для инициализации работы "консольного дисплея".
     */
    @Override
    protected void onCreate() {
        try {

            Thread updateConsoleThread = getUpdateConsoleThread();
            updateConsoleThread.start();

            super.onCreate();

            updateConsoleThread.interrupt();
            updateConsoleThread.join();
            displayResult(updatedX, updatedY);

        } catch (InterruptedException ignored) {
        }
    }

    @Override
    protected void applyChangedCoordinates(double x, double y) {
        updatedX = x;
        updatedY = y;
    }

    @Override
    protected void displayResult(double x, double y) {
        ConsoleHelper.printResult(updatedX, updatedY);
    }
}
