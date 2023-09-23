package com.PBA.budgetservice.controller.advice;

import com.PBA.budgetservice.exceptions.AuthorizationException;
import com.PBA.budgetservice.exceptions.BudgetException;
import com.PBA.budgetservice.exceptions.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiExceptionResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = this.getErrorMap(exception);
        return new ApiExceptionResponse(ZonedDateTime.now(), errorMap);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiExceptionResponse handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return new ApiExceptionResponse(ZonedDateTime.now(), Map.of(ErrorCodes.HTTP_MESSAGE_NOT_READABLE, exception.getMessage()));
    }

    @ExceptionHandler(BudgetException.class)
    public ResponseEntity<ApiExceptionResponse> handleBudgetException(BudgetException exception) {
        ApiExceptionResponse exceptionResponse = new ApiExceptionResponse(ZonedDateTime.now(), Map.of(exception.getErrorCode(), exception.getMessage()));
        return new ResponseEntity<>(exceptionResponse, exception.getHttpStatus());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Void> handleAuthorizationException(AuthorizationException exception) {
        return new ResponseEntity<>(exception.getHttpStatus());
    }

    private Map<String, String> getErrorMap(MethodArgumentNotValidException exception) {
        return exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
}
