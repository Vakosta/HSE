package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.basevalidators.StandartError;
import me.annenkov.formchecker.basevalidators.ValidationError;
import me.annenkov.formchecker.exceptions.WrongAnnotationException;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Абстрактный класс для описания валидатора аннотаций.
 * Для каждой аннотации написан свой класс валидатора, который
 * наследуется от {@code AnnotationValidator}.
 *
 * @param <A> Тип аннотации валидатора
 */
public abstract class AnnotationValidator<A extends Annotation> {
    private final Object fieldValue;
    private final A annotationType;

    private final List<Annotation> annotations;
    private final Set<String> acceptableTypes;
    private final String path;
    private String message;

    /**
     * Конструктор создаёт объект валидатора, основываясь на проверяемом
     * объекте, на его аннотациях и на его пути.
     *
     * @param object         Проверяемый объект
     * @param annotations    Аннотации объекта
     * @param path           Путь до объекта
     * @param annotationType Тип аннотации валидатора
     */
    protected AnnotationValidator(Object object, List<Annotation> annotations, String path, A annotationType) {
        this.fieldValue = object;
        this.annotationType = annotationType;

        this.annotations = annotations;
        this.acceptableTypes = new HashSet<>();

        this.path = path;
    }

    /**
     * Получает класс по его названию.
     *
     * @param className Название класса
     * @return Тип класса
     * @throws ClassNotFoundException Исключение сообщает, что
     *                                класс с таким названием найден не был
     */
    private static Class<?> parseType(final String className) throws ClassNotFoundException {
        String fqn = className.contains(".") ? className : "java.lang.".concat(className);
        return Class.forName(fqn);
    }

    protected Object getFieldValue() {
        return fieldValue;
    }

    protected A getAnnotationType() {
        return annotationType;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    protected void addAcceptableType(String acceptableType) {
        acceptableTypes.add(acceptableType);
    }

    protected String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected ValidationError getError() {
        return new StandartError(getMessage(), path, fieldValue);
    }

    /**
     * Проверяет, подходит ли аннотация к типу, у которого она указана.
     *
     * @throws WrongAnnotationException Исключение сообщает, что аннотация не подходит полю
     */
    protected void checkMatchTypes() {
        for (String acceptableType : acceptableTypes) {
            try {
                if (parseType(acceptableType).isInstance(fieldValue) ||
                        fieldValue.getClass().getName().equals(acceptableType)) {
                    return;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }

        throw new WrongAnnotationException(String.format("class %s is not accepted by %s annotation",
                fieldValue.getClass().getName(), annotationType.annotationType().getName()));
    }

    /**
     * Общий метод проверки поля.
     *
     * @return Ошибка, если она есть
     */
    public abstract Optional<ValidationError> validate();
}