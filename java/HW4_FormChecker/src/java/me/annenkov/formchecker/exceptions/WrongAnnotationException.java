package me.annenkov.formchecker.exceptions;

public class WrongAnnotationException extends RuntimeException {
    public WrongAnnotationException(String message) {
        super(message);
    }
}
