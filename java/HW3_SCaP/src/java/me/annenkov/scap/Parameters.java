package me.annenkov.scap;

/**
 * Класс обрабатывает и хранит пользовательские параметры.
 */
public class Parameters {
    private double x;
    private double y;
    private OutputMode outputMode;

    /**
     * Конструктор парсит и сохраняет в экземпляр переданные пользователем параметры.
     *
     * @param args Параметры, которые передал пользователь.
     */
    public Parameters(String[] args) {
        this.x = 0;
        this.y = 0;
        this.outputMode = OutputMode.STANDART;

        for (String s : args) {
            String[] arg = s.replace(" = ", "=").split("=");

            switch (arg[0]) {
                case "-x":
                    x = Double.parseDouble(arg[1]);
                    break;

                case "-y":
                    y = Double.parseDouble(arg[1]);
                    break;

                case "-o":
                    if (arg[1].equals("1"))
                        outputMode = OutputMode.STANDART;
                    else if (arg[1].equals("2"))
                        outputMode = OutputMode.ALL_ACTIONS;
                    break;

                default:
                    ConsoleHelper.writeLine("Игнорируется параметр: " + s);
                    break;
            }
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public OutputMode getOutputMode() {
        return outputMode;
    }

    /**
     * Enum для определения типа выводимой информации.
     */
    public enum OutputMode {
        STANDART,
        ALL_ACTIONS,
    }
}
