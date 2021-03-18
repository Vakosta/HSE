package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.annotations.NotEmpty;
import me.annenkov.formchecker.basevalidators.ValidationError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class NotEmptyValidator extends AnnotationValidator<NotEmpty> {
    public NotEmptyValidator(Object object, List<Annotation> annotations, String path) {
        super(object, annotations, path, (NotEmpty) annotations.stream().filter(a -> a.annotationType().getName()
                .equals(NotEmpty.class.getName())).findFirst().orElse(null));

        setMessage("Must not be empty");

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

        if (value == 0) {
            return Optional.of(getError());
        }

        return Optional.empty();
    }
}
