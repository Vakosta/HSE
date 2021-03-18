package me.annenkov.formchecker.basevalidators;

public interface ValidationError {
    String getMessage();

    String getPath();

    Object getFailedValue();
}
