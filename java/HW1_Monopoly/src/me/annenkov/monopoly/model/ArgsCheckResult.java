package me.annenkov.monopoly.model;

/**
 * @author <a href="mailto:vaannenkov@edu.hse.ru">Vladislav Annenkov</a>
 */
public class ArgsCheckResult {
    private final ArgsCheckType type;
    private String value;

    public ArgsCheckResult(ArgsCheckType type) {
        this.type = type;
    }

    public ArgsCheckResult(ArgsCheckType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Метод проверяет корректность переданных аргументов.
     *
     * @param args Аргументы, которые требууется проверить.
     * @return Объект, который содержит информацию о корректности аргументов.
     */
    public static ArgsCheckResult isArgsValid(String[] args) {
        if (args.length < 3)
            new ArgsCheckResult(ArgsCheckResult.ArgsCheckType.QUANTITY_ERROR);

        try {
            int width = Integer.parseInt(args[1]);
            int height = Integer.parseInt(args[0]);
            int money = Integer.parseInt(args[2]);

            if (width < 6 || width > 30 || height < 6 || height > 30 || money < 500 || money > 15000)
                return new ArgsCheckResult(ArgsCheckType.WRONG_BORDERS);
        } catch (NumberFormatException exception) {
            String value = exception.getMessage()
                    .replace("\"", "")
                    .replace("For input string: ", "");

            return new ArgsCheckResult(ArgsCheckResult.ArgsCheckType.NUMBER_FORMAT_ERROR, value);
        }

        return new ArgsCheckResult(ArgsCheckResult.ArgsCheckType.OK);
    }

    public ArgsCheckType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    /**
     * Результат проверки входных параметров.
     */
    public enum ArgsCheckType {
        OK,
        NUMBER_FORMAT_ERROR,
        QUANTITY_ERROR,
        WRONG_BORDERS,
    }
}
