package me.annenkov.formchecker.fieldvalidators;

import me.annenkov.formchecker.annotations.NotBlank;
import me.annenkov.formchecker.basevalidators.ValidationError;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class NotBlankValidator extends AnnotationValidator<NotBlank> {
    public NotBlankValidator(Object object, List<Annotation> annotations, String path) {
        super(object, annotations, path, (NotBlank) annotations.stream().filter(a -> a.annotationType().getName()
                .equals(NotBlank.class.getName())).findFirst().orElse(null));

        setMessage("Must not be blank");

        addAcceptableType("java.lang.String");
    }

    @Override
    public Optional<ValidationError> validate() {
        checkMatchTypes();

        if (((String) getFieldValue()).isBlank()) {
            return Optional.of(getError());
        }

        return Optional.empty();
    }
}