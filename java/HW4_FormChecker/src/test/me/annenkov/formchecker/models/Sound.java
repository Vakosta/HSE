package me.annenkov.formchecker.models;

import me.annenkov.formchecker.annotations.AnyOf;
import me.annenkov.formchecker.annotations.Constrained;
import me.annenkov.formchecker.annotations.NotBlank;
import me.annenkov.formchecker.annotations.NotNull;

@Constrained
public class Sound {
    @NotNull
    @NotBlank
    @AnyOf("pek-kek popug")
    private final String type;

    public Sound(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
