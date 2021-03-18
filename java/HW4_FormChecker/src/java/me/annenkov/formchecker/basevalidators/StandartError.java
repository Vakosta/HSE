package me.annenkov.formchecker.basevalidators;

public class StandartError implements ValidationError {
    private final String message;
    private final String path;
    private final Object failedValue;

    public StandartError(String message, String path, Object failedValue) {
        this.message = message;
        this.path = path;
        this.failedValue = failedValue;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Object getFailedValue() {
        return failedValue;
    }

    @Override
    public String toString() {
        return "StandartError{" +
                "message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", failedValue=" + failedValue +
                '}';
    }
}
