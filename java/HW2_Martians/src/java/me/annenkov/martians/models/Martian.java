package me.annenkov.martians.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Общий класс марсианина.
 *
 * @param <T> Тип генетического кода марсианина.
 */
public abstract class Martian<T> {
    protected MartianType type;
    protected Class<T> geneticCodeType;
    protected T geneticCode;
    protected Martian<T> parent;
    protected List<Martian<T>> children;

    public Martian(MartianType type, Class<T> geneticCodeType, T geneticCode) {
        this(type, geneticCodeType, geneticCode, null, new ArrayList<>());
    }

    public Martian(MartianType type, Class<T> geneticCodeType, T geneticCode, Martian<T> parent,
                   List<Martian<T>> children) {
        this.type = type;
        this.geneticCodeType = geneticCodeType;
        this.geneticCode = geneticCode;
        this.parent = parent;
        this.children = children;
    }

    /**
     * Метод получает марсианина-родителя для текущего марсианина.
     *
     * @return Марсианин-родитель.
     */
    public Martian<T> getParent() {
        return parent;
    }

    /**
     * Метод возвращает массив со всеми детьми.
     *
     * @return Массив детей.
     */
    public List<Martian<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Метод получает и возвращает массив со всеми потомками.
     *
     * @return Массив потомков.
     */
    public List<Martian<T>> getDescendants() {
        List<Martian<T>> descendants = new ArrayList<>(children);

        for (Martian<T> child : children)
            descendants.addAll(child.getDescendants());

        return Collections.unmodifiableList(descendants);
    }

    /**
     * Метод проверяет, существует ли ребёнок с переданным генетическим кодом.
     *
     * @param value Значение генетического кода.
     * @return true — если ребёнок существует, false — иначе.
     */
    public boolean hasChildWithValue(T value) {
        for (Martian<T> child : children)
            if (child.geneticCode.equals(value))
                return true;

        return false;
    }

    /**
     * Метод проверяет, существует ли потомок с переданным генетическим кодом.
     *
     * @param value Значение генетического кода.
     * @return true — если потомок существует, false — иначе.
     */
    public boolean hasDescendantWithValue(T value) {
        for (Martian<T> child : children)
            if (child.geneticCode.equals(value) || child.hasDescendantWithValue(value))
                return true;

        return false;
    }

    @Override
    public String toString() {
        return type.name + "(" +
                geneticCodeType.getName().replace("java.lang.", "") +
                ':' +
                geneticCode.toString() +
                ')';
    }

    /**
     * Тип марсианина.
     * Используется для обобщения строкового вывода при получении типа марсианина.
     */
    public enum MartianType {
        INNOVATOR("InnovatorMartian"),
        CONSERVATIVE("ConservativeMartian");

        private final String name;

        MartianType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
