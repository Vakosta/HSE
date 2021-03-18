package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.annotations.InRange;
import me.annenkov.formchecker.basevalidators.ValidationError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class InRangeValidator extends AnnotationValidator<InRange> {
    private final long min;
    private final long max;

    public InRangeValidator(Object object, List<Annotation> annotations, String path) {
        super(object, annotations, path, (InRange) annotations.stream().filter(a -> a.annotationType().getName()
                .equals(InRange.class.getName())).findFirst().orElse(null));

        this.min = getAnnotationType().min();
        this.max = getAnnotationType().max();

        setMessage("Must be in the declared range");

        addAcceptableType("byte");
        addAcceptableType("short");
        addAcceptableType("int");
        addAcceptableType("long");
        addAcceptableType("java.lang.Byte");
        addAcceptableType("java.lang.Short");
        addAcceptableType("java.lang.Integer");
        addAcceptableType("java.lang.Long");
    }

    @Override
    public Optional<ValidationError> validate() {
        if (getAnnotationType() == null) {
            return Optional.empty();
        }
        checkMatchTypes();

        long value = ((Number) getFieldValue()).longValue();
        if (value < min || value > max) {
            return Optional.of(getError());
        }

        return Optional.empty();
    }
}
