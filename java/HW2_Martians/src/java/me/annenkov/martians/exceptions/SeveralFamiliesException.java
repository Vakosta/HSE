package me.annenkov.martians.exceptions;

/**
 * Класс исключения, которое выбрасывается при наличии двух семей в отчёте.
 */
public class SeveralFamiliesException extends Exception {
    public SeveralFamiliesException() {
    }

    public SeveralFamiliesException(String message) {
        super(message);
    }

    public SeveralFamiliesException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeveralFamiliesException(Throwable cause) {
        super(cause);
    }

    public SeveralFamiliesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
