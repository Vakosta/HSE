package me.annenkov.formchecker.basevalidators;

import me.annenkov.formchecker.exceptions.AnnotationNotProvidedException;
import me.annenkov.formchecker.exceptions.WrongAnnotationException;
import me.annenkov.formchecker.models.Animal;
import me.annenkov.formchecker.models.BadModel;
import me.annenkov.formchecker.models.BadModelWithoutConstrained;
import me.annenkov.formchecker.models.GoodModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class StandartValidatorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateRightModel() {
        Animal animal = new Animal();
        Validator validator = new StandartValidator();

        Set<ValidationError> errors = validator.validate(animal);
        for (ValidationError error : errors) {
            System.out.println(error);
        }

        Assertions.assertEquals(errors.size(), 14);
    }

    @Test
    void validateWrongModel() {
        BadModel badModel = new BadModel();
        BadModelWithoutConstrained badModelWithoutConstrained = new BadModelWithoutConstrained();
        Validator validator = new StandartValidator();

        Assertions.assertThrows(WrongAnnotationException.class, () -> validator.validate(badModel));
        Assertions.assertThrows(AnnotationNotProvidedException.class, () -> validator.validate(badModelWithoutConstrained));
    }

    @Test
    void validateGoodModel() {
        GoodModel goodModel = new GoodModel();
        Validator validator = new StandartValidator();

        Assertions.assertDoesNotThrow(() -> validator.validate(goodModel));
    }
}