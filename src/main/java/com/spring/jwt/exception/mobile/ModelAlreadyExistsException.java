package com.spring.jwt.exception.mobile;

public class ModelAlreadyExistsException extends RuntimeException {
    public ModelAlreadyExistsException(String modelName) {
        super("Model already exists: " + modelName);
    }
}
