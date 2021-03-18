package me.annenkov.formchecker.models;

import me.annenkov.formchecker.annotations.Constrained;
import me.annenkov.formchecker.annotations.Positive;

@Constrained
public class BadModel {
    @Positive
    String kek = "";
}
