package com.spring.jwt.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxWordsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxWords {
    String message() default "Too many words";
    int value();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
