package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.annotations.NotNull;
import me.annenkov.formchecker.basevalidators.ValidationError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class NotNullValidator extends AnnotationValidator<NotNull> {
    public NotNullValidator(Object object, List<Annotation> annotations, String path) {
        super(object, annotations, path, (NotNull) annotations.stream().filter(a -> a.annotationType().getName()
                .equals(NotNull.class.getName())).findFirst().orElse(null));

        setMessage("Must not be null");
    }

    @Override
    public Optional<ValidationError> validate() {
        if (getFieldValue() == null) {
            return Optional.of(getError());
        }

        return Optional.empty();
    }
}
