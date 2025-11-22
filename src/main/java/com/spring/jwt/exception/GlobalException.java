package com.spring.jwt.exception;



import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.spring.jwt.auction.exception.AuctionException;
import com.spring.jwt.auction.exception.BidException;
import com.spring.jwt.exception.Bike.*;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class  GlobalException extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(UserNotFoundExceptions.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundExceptions exception, WebRequest webRequest){
        log.error("User not found: {}", exception.getMessage());
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePageNotFoundException(PageNotFoundException exception, WebRequest webRequest){
        log.error("Page not found: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmptyFieldException.class, UserAlreadyExistException.class})
    public ResponseEntity<ErrorResponseDto> handleCommonExceptions(RuntimeException exception, WebRequest webRequest) {
        log.error("Validation error: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidOtpException(InvalidOtpException exception, WebRequest webRequest){
        log.error("Invalid OTP: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ErrorResponseDto> handleOtpExpiredException(OtpExpiredException exception, WebRequest webRequest){
        log.error("OTP expired: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailNotVerifiedException(EmailNotVerifiedException exception, WebRequest webRequest){
        log.error("Email not verified: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException exception, WebRequest webRequest) {
        log.error("Authentication error: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.UNAUTHORIZED,
                "Authentication failed: " + exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException exception, WebRequest webRequest) {
        log.error("Bad credentials: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        log.error("Access denied: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.FORBIDDEN,
                "Access denied: You don't have permission to access this resource",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleDataAccessException(DataAccessException exception, WebRequest webRequest) {
        log.error("Database error: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Database error occurred",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // KEEP ONLY ONE handler for MethodArgumentTypeMismatchException!
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception, WebRequest request) {
        log.error("Type mismatch: {}", exception.getMessage());
        String error;
        if (exception.getRequiredType() != null) {
            error = exception.getName() + " should be of type " + exception.getRequiredType().getName();
        } else {
            error = exception.getName() + " has an invalid type";
        }
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                error,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Missing parameter: {}", ex.getMessage());
        String error = ex.getParameterName() + " parameter is missing";

        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getParameterName(), error);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Constraint violation: {}", ex.getMessage());
        return ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (existingMessage, newMessage) -> existingMessage + "; " + newMessage
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Resource Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllUncaughtException(Exception exception, WebRequest webRequest) {
        log.error("Uncaught error occurred: ", exception);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExamTimeWindowException.class)
    public ResponseEntity<ErrorResponseDto> handleExamTimeWindowException(ExamTimeWindowException exception, WebRequest webRequest){
        log.error("Exam time window error: {}", exception.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPaginationParameterException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPagination(InvalidPaginationParameterException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Invalid pagination parameters");
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotesNotCreatedException.class)
    public ResponseEntity<Map<String, Object>> handleNotesNotCreatedException(NotesNotCreatedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Notes Not Created");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(LaptopAlreadyExistsException.class)
    public ResponseEntity<LaptopResponseDTO> handleLaptopAlreadyExists(LaptopAlreadyExistsException ex,
                                                                      WebRequest request) {
        LaptopResponseDTO error = new LaptopResponseDTO();
        error.setApiPath(request.getDescription(false).replace("uri=", ""));
        error.setStatus("error");
        error.setMessage(ex.getMessage());
        error.setCode("ALREADY EXISTS");
        error.setStatusCode(HttpStatus.CONFLICT.value());
        error.setTimeStamp(LocalDateTime.now());
        error.setException(ex.toString());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public ResponseEntity<LaptopResponseDTO> handlePhotoNotFoundException(
            PhotoNotFoundException ex, WebRequest request) {

        LaptopResponseDTO error = new LaptopResponseDTO();

        error.setStatus("error");
        error.setMessage(ex.getMessage());
        error.setCode("NOT FOUND");
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setTimeStamp(LocalDateTime.now());
        error.setException(ex.toString());
        error.setApiPath(error.getApiPath());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CloudinaryDeleteException.class)
    public ResponseEntity<Map<String, Object>> handleCloudinaryDelete(CloudinaryDeleteException ex, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("apiPath", "uri=" + request.getRequestURI());
        error.put("errorCode", "CLOUDINARY_ERROR");
        error.put("errorMessage", ex.getMessage());
        error.put("errorTime", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    public ResponseEntity<LaptopResponseDTO> handelLaptopNotFoundException(LaptopNotFoundException ex, WebRequest webRequest){
        LaptopResponseDTO error = new LaptopResponseDTO();
        error.setApiPath(webRequest.getDescription(false).replace("uri=", ""));
        error.setStatus("error");
        error.setMessage(ex.getMessage());
        error.setCode("NOT FOUND");
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setTimeStamp(LocalDateTime.now());
        error.setException(ex.toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //Mobile Exception Handler
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
    public ResponseEntity<Map<String, Object>> handleMobileValidationException(
            MobileValidationException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

    }




// ========================= BIKE EXCEPTIONS =========================

    @ExceptionHandler(bikeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBikeNotFound(
            bikeNotFoundException ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_NOT_FOUND");
        errorResponse.put("errorMessage", ex.getMessage());
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBikeStatusNotFound(
            StatusNotFoundException ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_STATUS_NOT_FOUND");
        errorResponse.put("errorMessage", ex.getMessage());
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BikeImageNotFound.class)
    public ResponseEntity<Map<String, Object>> handleBikeImageNotFound(
            BikeImageNotFound ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_IMAGE_NOT_FOUND");
        errorResponse.put("errorMessage", "Bike Image Issue");
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidBikeData.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBikeData(
            InvalidBikeData ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("apiPath", request.getRequestURI());
        errorResponse.put("errorCode", "INVALID_BIKE_DATA");
        errorResponse.put("errorMessage", ex.getLocalizedMessage());
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }



    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            ResourceNotFound ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = Map.of(
                "apiPath", request.getRequestURI(),
                "errorCode", "RESOURCE_NOT_FOUND",
                "errorMessage", ex.getMessage(),
                "errorTime", LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        Map<String, Object> errorResponse = new HashMap<>();
        String message = ex.getLocalizedMessage();

        if (message != null && message.contains("registration_number")) {
            message = "Registration number already exists. Please provide a unique registration number.";
        } else {
            message = "An unexpected error occurred: " + message;
        }

        errorResponse.put("apiPath", "uri=" + request.getRequestURI());
        errorResponse.put("errorCode", "BIKE_OPERATION_FAILED");
        errorResponse.put("errorMessage", message);
        errorResponse.put("errorTime", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }







//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(
//            HttpMessageNotReadableException ex, WebRequest req) {
//
//        String message = "Invalid request payload.";
//
//        // Check if the cause is UnrecognizedPropertyException
//        if (ex.getCause() instanceof UnrecognizedPropertyException) {
//            UnrecognizedPropertyException cause = (UnrecognizedPropertyException) ex.getCause();
//            message = "Unknown field: " + cause.getPropertyName();
//        }
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//        body.put("error", "Invalid Field");
//        body.put("message", message);
//        body.put("path", req.getDescription(false));
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }


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

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookingNotFound(BookingNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Booking Not Found");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);


    }


    @ExceptionHandler(com.spring.jwt.auction.exception.AuctionException.class)
    public ResponseEntity<Map<String, Object>> handleAuctionException(AuctionException ex, WebRequest request) {
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Auction Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(com.spring.jwt.auction.exception.BidException.class)
    public ResponseEntity<Map<String, Object>> handleBidException(BidException ex, WebRequest request) {
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bid Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }


}





