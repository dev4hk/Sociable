package com.example.user.exception;

import com.example.user.enums.ErrorCode;
import com.example.user.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleException(LockedException ex) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(Response.error(ErrorCode.INVALID_PERMISSION.name()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleException(DisabledException ex) {
        return ResponseEntity.status(UNAUTHORIZED)
                .body(Response.error(ErrorCode.INVALID_PERMISSION.name()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleException(BadCredentialsException ex) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(Response.error(ErrorCode.BAD_CREDENTIAL.name()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        Set<String> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });
        return ResponseEntity.status(BAD_REQUEST)
                .body(
                        Response.error(ErrorCode.INVALID_REQUEST.name(), errors)
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> applicationHandler(Exception error) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }
}
