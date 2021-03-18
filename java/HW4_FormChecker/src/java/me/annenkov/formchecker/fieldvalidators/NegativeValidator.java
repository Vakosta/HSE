package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.annotations.Negative;
import me.annenkov.formchecker.basevalidators.ValidationError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class NegativeValidator extends AnnotationValidator<Negative> {
    public NegativeValidator(Object object, List<Annotation> annotations, String path) {
        super(object, annotations, path, (Negative) annotations.stream().filter(a -> a.annotationType().getName()
                .equals(Negative.class.getName())).findFirst().orElse(null));

        setMessage("Must be negative");

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
        checkMatchTypes();

        if (((Number) getFieldValue()).longValue() >= 0) {
            return Optional.of(getError());
        }

        return Optional.empty();
    }
}
