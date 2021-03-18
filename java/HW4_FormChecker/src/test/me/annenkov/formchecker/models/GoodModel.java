package me.annenkov.formchecker.models;

import me.annenkov.formchecker.annotations.Constrained;
import me.annenkov.formchecker.annotations.NotNull;

@Constrained
public class GoodModel {
    @NotNull
    private BadModelWithoutConstrained badModel;
}
