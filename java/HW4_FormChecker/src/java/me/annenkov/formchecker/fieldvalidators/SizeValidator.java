package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.annotations.Size;
import me.annenkov.formchecker.basevalidators.ValidationError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SizeValidator extends AnnotationValidator<Size> {
    private final int min;
    private final int max;

    public SizeValidator(Object object, List<Annotation> annotations, String path) {
        super(object, annotations, path, (Size) annotations.stream().filter(a -> a.annotationType().getName()
                .equals(Size.class.getName())).findFirst().orElse(null));

        this.min = getAnnotationType().min();
        this.max = getAnnotationType().max();

        setMessage("Must be within the declared size");

        addAcceptableType("java.util.List");
        addAcceptableType("java.util.Set");
        addAcceptableType("java.util.Map");
        addAcceptableType("java.lang.String");
    }

    @Override
    public Optional<ValidationError> validate() {
        checkMatchTypes();

        int value;
        if (getFieldValue() instanceof List) {
            value = ((List<?>) getFieldValue()).size();
        } else if (getFieldValue() instanceof Set) {
            value = ((Set<?>) getFieldValue()).size();
        } else if (getFieldValue() instanceof Map) {
            value = ((Map<?, ?>) getFieldValue()).size();
        } else {
            value = ((String) getFieldValue()).length();
        }

        if (value < min || value > max) {
            return Optional.of(getError());
        }

        return Optional.empty();
    }
}