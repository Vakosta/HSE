package me.annenkov.formchecker.models;

import me.annenkov.formchecker.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Constrained
public class Animal {
    @Negative
    int kek = 123;

    @NotNull
    @NotEmpty
    private List<@NotNull Sound> keks;

    @Size(max = 5)
    private List<@Positive @InRange(min = -1, max = 8) Integer> lols;

    public Animal() {
        keks = new ArrayList<>();
        keks.add(new Sound("123"));
        keks.add(new Sound("143"));
        keks.add(new Sound("kek"));
        keks.add(new Sound("cheburek"));
        keks.add(new Sound("     "));
        keks.add(new Sound("pek-kek popug"));
        keks.add(new Sound(null));

        lols = new ArrayList<>();
        lols.add(123);
        lols.add(432);
        lols.add(4);
        lols.add(2);
        lols.add(-1);
        lols.add(-2);
    }
}
