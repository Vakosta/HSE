package me.annenkov.formchecker.basevalidators;

import java.util.Set;

public interface Validator {
    Set<ValidationError> validate(Object object);
}