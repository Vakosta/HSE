package me.annenkov.martians;

import me.annenkov.martians.exceptions.SeveralFamiliesException;
import me.annenkov.martians.models.ConservatorMartian;
import me.annenkov.martians.models.InnovatorMartian;
import me.annenkov.martians.models.Martian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Утилитный класс дерева для взаимодействия с марсианами.
 *
 * @param <T> Тип генетического кода марсианина.
 * @param <M> Тип самого марсианина.
 */
@SuppressWarnings("unchecked")
public class FamilyTree<T, M extends Martian<T>> {
    private final List<M> martians;

    public FamilyTree(List<M> martians) {
        this.martians = martians;
    }

    public FamilyTree(M martian) {
        M parentMartian = martian;
        while (parentMartian.getParent() != null)
            parentMartian = (M) parentMartian.getParent();

        List<M> martiansFamily = new ArrayList<>((Collection<? extends M>) parentMartian.getDescendants());
        martiansFamily.add(parentMartian);

        this.martians = martiansFamily;
    }

    /**
     * Метод для получения объекта FamilyTree из переданной строки.
     *
     * @param type   Класс типа генетического кода в семье.
     * @param report Строка с семьёй из марсиан.
     * @param <T>    Тип генетического кода в семье.
     * @param <M>    Тип марсиан в семье.
     * @return Объект FamilyTree.
     * @throws SeveralFamiliesException Исключение бросается при наличии нескольких
     *                                  семей в переданной строке.
     */
    public static <T, M extends Martian<T>> FamilyTree<T, M> getInstance(Class<T> type, String report)
            throws SeveralFamiliesException {
        return convertStringToTree(type, report, 0, null, null, null);
    }

    /**
     * Метод получает тип генетического семьи по переданной строке.
     *
     * @param text Строка с семьёй из марсиан.
     * @return Класс типа генетического кода в семье.
     */
    public static Class<?> getCodeTypeByString(String text) {
        if (text.contains("(Integer:"))
            return Integer.class;
        if (text.contains("(Double:"))
            return Double.class;
        return String.class;
    }

    /**
     * Метод получает тип марсиан в семье по переданной строке.
     *
     * @param text Строка с семьёй из марсиан.
     * @return Класс типа генетического кода в семье.
     */
    public static Martian.MartianType getMartianTypeByString(String text) {
        if (text.contains("InnovatorMartian("))
            return Martian.MartianType.INNOVATOR;
        return Martian.MartianType.CONSERVATIVE;
    }

    /**
     * Метод превращает отдельную строку с марсианином в объект марсианина.
     *
     * @param type        Тип класса генетического кода марсианина.
     * @param martianLine Строка с марсианином.
     * @return Объект-марсианина.
     */
    private static InnovatorMartian<?> convertStringToMartian(Class<?> type, String martianLine) {
        martianLine = martianLine.trim();
        String[] typeAndCode = martianLine.split("Martian");
        typeAndCode[1] = typeAndCode[1].substring(1, typeAndCode[1].length() - 1);

        String code = typeAndCode[1].split(":")[1];

        if (type == Integer.class)
            return new InnovatorMartian<>(Integer.class, Integer.parseInt(code));
        if (type == Double.class)
            return new InnovatorMartian<>(Double.class, Double.parseDouble(code));
        return new InnovatorMartian<>(String.class, code);
    }

    /**
     * Метод получает уровень вложенности марсианина по переданной строке.
     *
     * @param line Строка с марсианином.
     * @return Одно число — уровень вложенности.
     */
    private static int getMartianLevelByString(String line) {
        int counter = 0;
        while (line.charAt(counter) == ' ')
            counter++;
        return counter / 4;
    }

    /**
     * Метод конвертирует строку с семьёй в объект FamilyTree.
     *
     * @param type          Класс типа генетического кода марсиан в семье.
     * @param report        Строка с марсианской семьёй.
     * @param level         Уровень итерации.
     * @param parent        Марсианин-родитель.
     * @param localMartians Следующие марсиане после обрабатываемого.
     * @param lines         Строки с марсианами.
     * @param <T>           Тип генетического кода марсиан в семье.
     * @param <M>           Тип марсиан в семье.
     * @return Объект FamilyTree.
     * @throws SeveralFamiliesException Исключение бросается, если в строке присутствует больше одной семьи.
     */
    private static <T, M extends Martian<T>> FamilyTree<T, M> convertStringToTree(
            Class<T> type,
            String report,
            int level,
            Martian<T> parent,
            List<InnovatorMartian<T>> localMartians,
            List<String> lines
    ) throws SeveralFamiliesException {
        if (lines == null) {
            lines = Arrays.asList(report.split("\n"));
            localMartians = new ArrayList<>();

            for (String line : lines)
                localMartians.add((InnovatorMartian<T>) convertStringToMartian(type, line));
        }

        int counter = 0;
        for (String line : lines) {
            if (getMartianLevelByString(line) == level) {
                if (level == 0 && counter > 0)
                    throw new SeveralFamiliesException("A family tree can only consist of one family!");

                if (parent != null)
                    localMartians.get(counter).setParent(parent);

                convertStringToTree(type,
                        report,
                        level + 1,
                        localMartians.get(counter),
                        localMartians.subList(counter + 1, localMartians.size()),
                        lines.subList(counter + 1, lines.size()));
            }
            if (getMartianLevelByString(line) < level)
                return null;
            counter++;
        }

        if (getMartianTypeByString(report) == Martian.MartianType.CONSERVATIVE)
            return new FamilyTree<>((M) new ConservatorMartian<>(localMartians.get(0)));

        return new FamilyTree<>((List<M>) localMartians);
    }

    /**
     * Метод получает марсиан-родителей.
     *
     * @return Список с марсианами-родителями.
     */
    private List<Martian<T>> getParentMartians() {
        List<Martian<T>> result = new ArrayList<>();
        for (Martian<T> martian : martians)
            if (martian.getParent() == null)
                result.add(martian);
        return result;
    }

    /**
     * Метод возвращает текстовый отчёт с марсианами.
     *
     * @return Текстовый отчёт.
     */
    public String getTextReport() {
        return getTextReport(0, null);
    }

    /**
     * Метод возвращает текстовый отчёт с марсианами.
     *
     * @param level           Уровень вложенности рекурсии.
     * @param currentMartians Текущий обрабатываемый марсианин.
     * @return Текстовый отчёт.
     */
    private String getTextReport(int level, List<Martian<T>> currentMartians) {
        StringBuilder result = new StringBuilder();

        if (currentMartians == null)
            currentMartians = getParentMartians();

        for (Martian<T> currentMartian : currentMartians) {
            result.append("    ".repeat(Math.max(0, level)));
            result.append(currentMartian.toString()).append('\n');
            result.append(getTextReport(level + 1, currentMartian.getChildren()));
        }

        return result.toString();
    }
}
