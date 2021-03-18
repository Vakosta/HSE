package me.annenkov.formchecker.basevalidators;

import me.annenkov.formchecker.annotations.*;
import me.annenkov.formchecker.exceptions.AnnotationNotProvidedException;
import me.annenkov.formchecker.fieldvalidators.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class StandartValidator implements Validator {
    /**
     * Проверяет для конкретного поля/объекта его соответствие
     * к указанным аннотациям.
     *
     * @param object      Объект, который требуется проверить
     * @param annotations Декларированные аннотации объекта
     * @param path        Путь до объекта
     * @return Set ошибок, которые были обнаружены
     */
    private Set<ValidationError> checkAnnotations(Object object, List<Annotation> annotations, String path) {
        Set<ValidationError> errors = new HashSet<>();

        if (annotations.stream().anyMatch(NotNull.class::isInstance))
            new NotNullValidator(object, annotations, path).validate().ifPresent(errors::add);

        if (errors.size() > 0)
            return errors;

        if (annotations.stream().anyMatch(Positive.class::isInstance))
            new PositiveValidator(object, annotations, path).validate().ifPresent(errors::add);
        if (annotations.stream().anyMatch(Negative.class::isInstance))
            new NegativeValidator(object, annotations, path).validate().ifPresent(errors::add);
        if (annotations.stream().anyMatch(NotBlank.class::isInstance))
            new NotBlankValidator(object, annotations, path).validate().ifPresent(errors::add);
        if (annotations.stream().anyMatch(NotEmpty.class::isInstance))
            new NotEmptyValidator(object, annotations, path).validate().ifPresent(errors::add);
        if (annotations.stream().anyMatch(Size.class::isInstance))
            new SizeValidator(object, annotations, path).validate().ifPresent(errors::add);
        if (annotations.stream().anyMatch(InRange.class::isInstance))
            new InRangeValidator(object, annotations, path).validate().ifPresent(errors::add);
        if (annotations.stream().anyMatch(AnyOf.class::isInstance))
            new AnyOfValidator(object, annotations, path).validate().ifPresent(errors::add);

        return errors;
    }

    /**
     * Проверяет поле внутри класса.
     *
     * @param baseObject Класс, чьё поле требуется проверить
     * @param field      Само поле класса
     * @param path       Путь до поля класса
     * @return Set ошибок, которые были обнаружены
     */
    private Set<ValidationError> validateField(Object baseObject, Field field, String path) {
        try {
            field.setAccessible(true);
            List<Annotation> annotations = new ArrayList<>(List.of(field.getAnnotatedType().getDeclaredAnnotations()));
            annotations.addAll(List.of(field.getDeclaredAnnotations()));
            Set<ValidationError> errors = new HashSet<>(checkAnnotations(field.get(baseObject), annotations, path));

            Object fieldData = field.get(baseObject);
            if (fieldData instanceof Collection) {
                errors.addAll(validateCollectionData((Collection<?>) fieldData, field, path));
            } else {
                try {
                    errors.addAll(validate(fieldData));
                } catch (Exception ignored) {
                }
            }

            return errors;
        } catch (IllegalAccessException exception) {
            return new HashSet<>();
        }
    }

    /**
     * Проверяет объект отдельный объект внутри коллекции.
     *
     * @param object        Объект, который требуется проверить
     * @param annotatedType Тип объекта
     * @param path          Путь до объекта
     * @return Set ошибок, которые были обнаружены
     */
    private Set<ValidationError> validateListField(Object object, AnnotatedType annotatedType, String path) {
        List<Annotation> annotations = List.of(annotatedType.getAnnotations());

        Set<ValidationError> errors = new HashSet<>(checkAnnotations(object, annotations, path));

        if (object instanceof Collection) {
            errors.addAll(validateCollectionData((Collection<?>) object,
                    (AnnotatedParameterizedType) annotatedType, path));
        } else {
            try {
                errors.addAll(validate(object, path));
            } catch (Exception ignored) {
            }
        }

        return errors;
    }

    /**
     * Проверяет все объекты внутри коллекции. Метод используется для рекурсивного
     * вызова вложенных коллекций, когда поле {@code field} становится недоступно.
     *
     * @param collection                 Коллекция, в которой требуется проверить объекты
     * @param annotatedParameterizedType Тип коллекции
     * @param path                       Путь до коллекции
     * @return Set ошибок, которые были обнаружены
     */
    private Set<ValidationError> validateCollectionData(Collection<?> collection,
                                                        AnnotatedParameterizedType annotatedParameterizedType,
                                                        String path) {
        AnnotatedType annotatedType = annotatedParameterizedType.getAnnotatedActualTypeArguments()[0];

        Set<ValidationError> errors = new HashSet<>();

        int index = 0;
        for (Object o : collection) {
            try {
                errors.addAll(validateListField(o, annotatedType, path.concat("[" + index + "]")));
            } catch (Exception ignored) {
            }
            index++;
        }

        return errors;
    }

    /**
     * Проверяет все объекты внутри коллекции.
     *
     * @param collection      Коллекция, в которой требуется проверить объекты
     * @param collectionField Поле коллекции
     * @param path            Путь до коллекции
     * @return Set ошибок, которые были обнаружены
     */
    private Set<ValidationError> validateCollectionData(Collection<?> collection, Field collectionField, String path) {
        AnnotatedParameterizedType annotatedType = (AnnotatedParameterizedType) collectionField.getAnnotatedType();
        return validateCollectionData(collection, annotatedType, path);
    }

    /**
     * Проверяет переданный объект. Метод используется для рекурсивного вызова.
     * В качестве дополнительного параметра передаётся {@code path}, который
     * используется для построения пути в ошибке.
     *
     * @param object Объект, который требуется проверить
     * @param path   Путь до объекта
     * @return Set ошибок, которые были обнаружены
     */
    public Set<ValidationError> validate(Object object, String path) {
        Class<?> objectClass = requireNonNull(object).getClass();
        if (!objectClass.isAnnotationPresent(Constrained.class) && path.isBlank()) {
            throw new AnnotationNotProvidedException();
        } else if (!objectClass.isAnnotationPresent(Constrained.class)) {
            return Set.of();
        }

        Set<ValidationError> errors = new HashSet<>();
        for (Field field : objectClass.getDeclaredFields()) {
            if (path.isBlank())
                errors.addAll(validateField(object, field, path.concat(field.getName())));
            else
                errors.addAll(validateField(object, field, path.concat("." + field.getName())));
        }

        return errors;
    }

    /**
     * Проверяет переданный объект.
     *
     * @param object Объект, который требуется проверить
     * @return Set ошибок, которые были обнаружены
     */
    @Override
    public Set<ValidationError> validate(Object object) {
        return validate(object, "");
    }
}