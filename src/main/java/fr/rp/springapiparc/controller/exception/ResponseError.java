package fr.rp.springapiparc.controller.exception;

import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ResponseError {

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Object> handleNoResultException(NoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append("champ : " + fieldError.getField() + " - erreur : " + fieldError.getDefaultMessage() + " \n ");
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage.toString());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}
