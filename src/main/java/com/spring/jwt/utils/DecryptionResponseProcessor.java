package com.spring.jwt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.dto.ResponseAllUsersDto;
import com.spring.jwt.dto.UserDTO;
import com.spring.jwt.laptop.dto.ImageUploadResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.Map;

/**
 * This class intercepts all responses from controllers and ensures sensitive data is decrypted
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class DecryptionResponseProcessor implements ResponseBodyAdvice<Object> {

    private final EncryptionUtil encryptionUtil;

    /**
     * We allow all responses here,
     * filtering is safely handled in beforeBodyWrite().
     */
    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {


        if (body == null) {
            return null;
        }

        if (body instanceof ProblemDetail) {
            return body;
        }

        if (body instanceof ImageUploadResponseDTO) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return body;
        }


        if (!MediaType.APPLICATION_JSON.includes(selectedContentType)) {
            return body;
        }

        try {
            log.debug("Decrypting response body: {}", body.getClass().getName());
            return processResponse(body);
        } catch (Exception ex) {
            log.error("Error while decrypting response", ex);
            return body; // never break response pipeline
        }
    }


    private Object processResponse(Object body) {

        // Skip JPA entities
        if (body.getClass().isAnnotationPresent(Entity.class)) {
            return body;
        }

        // Skip Hibernate persistent collections
        if (body instanceof org.hibernate.collection.spi.PersistentCollection) {
            return body;
        }

        // Handle ResponseAllUsersDto
        if (body instanceof ResponseAllUsersDto responseDto) {
            if (responseDto.getList() != null) {
                responseDto.getList().forEach(this::decryptUserDTO);
            }
            return body;
        }

        // Handle single UserDTO
        if (body instanceof UserDTO userDTO) {
            decryptUserDTO(userDTO);
            return body;
        }

        // Handle List responses
        if (body instanceof List<?> list) {
            list.forEach(this::processResponse);
            return body;
        }

        // Handle Map responses
        if (body instanceof Map<?, ?> map) {
            map.values().forEach(this::processResponse);
            return body;
        }

        // Default: return untouched
        return body;
    }


    private void decryptUserDTO(UserDTO user) {
        try {
            if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
                user.setFirstName(encryptionUtil.decrypt(user.getFirstName()));
            }

            if (user.getLastName() != null && !user.getLastName().isBlank()) {
                user.setLastName(encryptionUtil.decrypt(user.getLastName()));
            }

            if (user.getAddress() != null && !user.getAddress().isBlank()) {
                user.setAddress(encryptionUtil.decrypt(user.getAddress()));
            }
        } catch (Exception e) {
            log.error("Failed to decrypt UserDTO fields", e);
        }
    }

} 