package me.annenkov.martians.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс марсианина-консерватора.
 *
 * @param <T> Тип генетического кода марсианина.
 */
public class ConservatorMartian<T> extends Martian<T> {
    private final Martian<T> parent;
    private final List<Martian<T>> children;

    public ConservatorMartian(InnovatorMartian<T> innovatorMartian) {
        super(MartianType.CONSERVATIVE, innovatorMartian.geneticCodeType, innovatorMartian.geneticCode);
        if (innovatorMartian.parent != null)
            this.parent = new ConservatorMartian<>((InnovatorMartian<T>) innovatorMartian.parent);
        else
            this.parent = null;

        this.children = new ArrayList<>();
        for (Martian<T> child : innovatorMartian.getChildren())
            this.children.add(new ConservatorMartian<>((InnovatorMartian<T>) child, this));
    }

    public ConservatorMartian(InnovatorMartian<T> innovatorMartian, ConservatorMartian<T> parent) {
        super(MartianType.CONSERVATIVE, innovatorMartian.geneticCodeType, innovatorMartian.geneticCode);
        this.parent = parent;

        this.children = new ArrayList<>();
        for (Martian<T> child : innovatorMartian.getChildren())
            this.children.add(new ConservatorMartian<>((InnovatorMartian<T>) child, this));
    }

    @Override
    public Martian<T> getParent() {
        return parent;
    }

    @Override
    public List<Martian<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
