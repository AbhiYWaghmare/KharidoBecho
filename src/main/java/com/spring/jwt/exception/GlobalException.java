
package com.spring.jwt.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.spring.jwt.car.auction.exception.CarAuctionNotFoundException;
import com.spring.jwt.car.auction.exception.CarInvalidAuctionStateException;
import com.spring.jwt.exception.car.*;
import com.spring.jwt.exception.car.BuyerNotFoundException;
import com.spring.jwt.exception.car.SellerNotFoundException;
import com.spring.jwt.exception.mobile.*;
import com.spring.jwt.exception.laptop.*;
import com.spring.jwt.laptop.dto.LaptopResponseDTO;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.ErrorResponseDTO1;
import com.spring.jwt.utils.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalException extends ResponseEntityExceptionHandler {

    // Invalid JSON OR unknown fieldBookingNotFoundException
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = "Invalid request payload.";
        if (ex.getCause() instanceof UnrecognizedPropertyException cause) {
            message = "Unknown field: " + cause.getPropertyName();
        }

        ErrorResponseDTO1 response = new ErrorResponseDTO1(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Field",
                message,
                request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Base Custom Exception (code + message)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponseDTO> handleBaseException(BaseException e) {
        log.error("Base exception occurred: {}", e.getMessage());

        BaseResponseDTO response = BaseResponseDTO.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();

        HttpStatus status;
        try {
            status = HttpStatus.valueOf(Integer.parseInt(e.getCode()));
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }

    // USER
    @ExceptionHandler(UserNotFoundExceptions.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(
            UserNotFoundExceptions exception, WebRequest webRequest) {

        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmptyFieldException.class, UserAlreadyExistException.class})
    public ResponseEntity<ErrorResponseDto> handleUserValidationErrors(
            RuntimeException exception, WebRequest webRequest) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    // OTP, EMAIL
    @ExceptionHandler({InvalidOtpException.class, OtpExpiredException.class, EmailNotVerifiedException.class})
    public ResponseEntity<ErrorResponseDto> handleOtpErrors(
            RuntimeException exception, WebRequest webRequest) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    // AUTHENTICATION
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(
            AuthenticationException exception, WebRequest webRequest) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.UNAUTHORIZED,
                "Authentication failed: " + exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(
            BadCredentialsException exception, WebRequest webRequest) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
            AccessDeniedException exception, WebRequest webRequest) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.FORBIDDEN,
                "Access denied: You don't have permission",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    // DATABASE EXCEPTIONS
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleDataAccessException(
            DataAccessException exception, WebRequest webRequest) {

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Database error occurred",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
            DataIntegrityViolationException ex, WebRequest req) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("message", "Duplicate or invalid database entry.");
        body.put("path", req.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }


    // TYPE MISMATCH
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception, WebRequest request) {

        String error = exception.getName() + " has invalid type";

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                error,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    // MISSING PARAMS
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getParameterName(), ex.getParameterName() + " is required");

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // DTO FIELD VALIDATION
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();

        errors.forEach(error -> {
            String field = ((FieldError) error).getField();
            validationErrors.put(field, error.getDefaultMessage());
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolation(ConstraintViolationException ex) {

        return ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage(),
                        (msg1, msg2) -> msg1 + ", " + msg2
                ));
    }

    // RESOURCE NOT FOUND
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Resource Not Found");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // EXAM WINDOW
    @ExceptionHandler(ExamTimeWindowException.class)
    public ResponseEntity<ErrorResponseDto> handleExamWindow(
            ExamTimeWindowException exception, WebRequest webRequest) {

        ErrorResponseDto error = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // PAGINATION
    @ExceptionHandler(InvalidPaginationParameterException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPagination(
            InvalidPaginationParameterException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("error", "Invalid pagination parameters");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // NOTES
    @ExceptionHandler(NotesNotCreatedException.class)
    public ResponseEntity<Map<String, Object>> handleNotesNotCreated(NotesNotCreatedException ex) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Notes Not Created");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // LAPTOP
    @ExceptionHandler(LaptopAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleLaptopAlreadyExists(
            LaptopAlreadyExistsException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Laptop Already Exists");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LaptopNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLaptopNotFound(
            LaptopNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Laptop Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePhotoNotFoundException(
            PhotoNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Photo Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CloudinaryDeleteException.class)
    public ResponseEntity<Map<String, Object>> handleCloudinaryDelete(
            CloudinaryDeleteException ex, HttpServletRequest req) {

        Map<String, Object> error = new HashMap<>();
        error.put("apiPath", "uri=" + req.getRequestURI());
        error.put("errorCode", "CLOUDINARY_ERROR");
        error.put("errorMessage", ex.getMessage());
        error.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    // MOBILE
    @ExceptionHandler(MobileNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMobileNotFound(
            MobileNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Mobile Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MobileImageException.class)
    public ResponseEntity<Map<String, Object>> handleMobileImageException(
            MobileImageException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Mobile Image Upload Failed");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSellerNotFound(
            SellerNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Seller Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BuyerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBuyerNotFound(
            BuyerNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Buyer Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MobileValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMobileValidation(
            MobileValidationException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // CAR
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCarNotFound(
            CarNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Car Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CarImageException.class)
    public ResponseEntity<Map<String, Object>> handleCarImage(
            CarImageException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Car Image Upload Failed");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CarValidationException.class)
    public ResponseEntity<Map<String, Object>> handleCarValidation(
            CarValidationException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookingNotFound(
            BookingNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Booking Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
//    @ExceptionHandler(CarAuctionException.class)
//    public ResponseEntity<Map<String, Object>> handleCarAuctionException(CarAuctionException ex, WebRequest req) {
//        Map<String, Object> error = new HashMap<>();
//        error.put("message", ex.getMessage());
//        error.put("timeStamp", LocalDateTime.now());
//        error.put("apiPath", req.getDescription(false).replace("uri=", ""));
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(CarInvalidAuctionStateException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCarAuctionState(CarInvalidAuctionStateException ex, WebRequest req) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("timeStamp", LocalDateTime.now());
        error.put("apiPath", req.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CarAuctionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCarAuctionNotFound(CarAuctionNotFoundException ex, WebRequest req) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("timeStamp", LocalDateTime.now());
        error.put("apiPath", req.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateBooking(
            DuplicateBookingException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Duplicate Booking");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidBookingOperationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBookingOperation(
            InvalidBookingOperationException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Invalid Booking Operation");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PendingBookingException.class)
    public ResponseEntity<Map<String, Object>> handlePendingBooking(
            PendingBookingException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Pending Booking");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // FALLBACK EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllUncaughtException(
            Exception exception, WebRequest webRequest) {

        log.error("Uncaught error: ", exception);

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

