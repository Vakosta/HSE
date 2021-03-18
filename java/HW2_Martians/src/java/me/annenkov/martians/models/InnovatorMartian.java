package me.annenkov.martians.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс марсианина-новатора.
 *
 * <p>
 * Основные возможности новаторов:
 * <ul>
 *     <li>Могут устанавливать чужих родителей или детей.</li>
 *     <li>Могут изменять значение генетического кода со следующими условиями:</li>
 *     <ul>
 *         <li>Нельзя оказаться своим же потомком,</li>
 *         <li>Нельзя выдавать чужих детей за своих.</li>
 *     </ul>
 * </ul>
 * </p>
 *
 * @param <T> Тип генетического кода марсианина.
 */
public class InnovatorMartian<T> extends Martian<T> {
    public InnovatorMartian(Class<T> geneticCodeType, T geneticCode) {
        super(MartianType.INNOVATOR, geneticCodeType, geneticCode, null, new ArrayList<>());
    }

    /**
     * Метод устанавливает генетический код марсианину.
     *
     * @param geneticCode Генетический код.
     */
    public void setGeneticCode(T geneticCode) {
        this.geneticCode = geneticCode;
    }

    /**
     * Устанавливает марсианину родителя.
     *
     * @param parent Родитель, которого требуется установить.
     * @return true — если установка прошла успешно, false — иначе.
     */
    public boolean setParent(Martian<T> parent) {
        if (parent == null) {
            this.parent = null;
            return true;
        }

        if (parent.type != MartianType.INNOVATOR)
            return false;

        return ((InnovatorMartian<T>) parent).addChild(this);
    }

    /**
     * Устанавливает массив детей марсианину.
     *
     * @param children Массив детей.
     * @return true — если установка прошла успешно, false — иначе.
     */
    public boolean setChildren(List<Martian<T>> children) {
        this.children.clear();
        for (Martian<T> child : children)
            if (!addChild(child))
                return false;

        return true;
    }

    /**
     * Метод добавляет ребёнка марсианину.
     *
     * @param childMartian Дочерний марсианин.
     * @return true — если добавление прошло успешно, false — иначе.
     */
    public boolean addChild(Martian<T> childMartian) {
        if (
                this.type != MartianType.INNOVATOR
                        || childMartian.type != MartianType.INNOVATOR
                        || childMartian.equals(this)
                        || this.hasDescendantWithValue(childMartian.geneticCode)
                        || childMartian.hasDescendantWithValue(this.geneticCode)
        )
            return false;

        if (childMartian.parent != null)
            ((InnovatorMartian<T>) childMartian.parent).removeChild(childMartian);

        childMartian.parent = this;
        this.children.add(childMartian);

        return true;
    }

    /**
     * Метод удаляет ребёнка у марсианина.
     *
     * @param childMartian Ребёнок, которого требуется удалить.
     * @return true — если удаление прошло успешно, false — иначе.
     */
    public boolean removeChild(Martian<T> childMartian) {
        if (
                this.type != MartianType.INNOVATOR
                        || childMartian.type != MartianType.INNOVATOR
                        || this.equals(childMartian)
                        || !this.hasChildWithValue(childMartian.geneticCode)
        )
            return false;

        return children.remove(childMartian) && ((InnovatorMartian<T>) childMartian).setParent(null);
    }
}
