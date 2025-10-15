package com.spring.jwt.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxWordsValidator implements ConstraintValidator<MaxWords, String> {
    private int max;

    @Override
    public void initialize(MaxWords constraintAnnotation) {
        this.max = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        String[] words = value.trim().split("\\s+");
        return words.length <= max;
    }
}
