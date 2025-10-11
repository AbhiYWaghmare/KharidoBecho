package com.spring.jwt.utils.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinWordsValidator implements ConstraintValidator<MinWords, String> {
    private int min;

    @Override
    public void initialize(MinWords constraintAnnotation) {
        this.min = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        String[] words = value.trim().split("\\s+");
        return words.length >= min;
    }
}
