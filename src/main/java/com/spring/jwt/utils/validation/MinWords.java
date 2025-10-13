package com.spring.jwt.utils.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinWordsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinWords {
    String message() default "Too few words";
    int value();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
