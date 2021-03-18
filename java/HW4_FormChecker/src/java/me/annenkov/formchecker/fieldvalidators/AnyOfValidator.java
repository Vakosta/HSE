package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.annotations.AnyOf;
import me.annenkov.formchecker.basevalidators.ValidationError;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AnyOfValidator extends AnnotationValidator<AnyOf> {
    private List<String> values;

    public AnyOfValidator(Object object, List<Annotation> annotations, String path) {
        super(object, annotations, path, (AnyOf) annotations.stream().filter(a -> a.annotationType().getName()
                .equals(AnyOf.class.getName())).findFirst().orElse(null));

        this.values = new ArrayList<>(Arrays.asList(getAnnotationType().value()));

        setMessage("Must be any of the declared values");

        addAcceptableType("java.lang.String");
    }

    @Override
    public Optional<ValidationError> validate() {
        if (getAnnotationType() == null) {
            return Optional.empty();
        }
        checkMatchTypes();

        String value = (String) getFieldValue();
        if (!values.contains(value)) {
            return Optional.of(getError());
        }

        return Optional.empty();
    }
}
