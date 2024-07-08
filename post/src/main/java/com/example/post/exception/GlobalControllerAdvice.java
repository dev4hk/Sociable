package com.example.post.exception;

import com.example.post.enums.ErrorCode;
import com.example.post.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(PostException.class)
    public ResponseEntity<?> applicationHandler(PostException error) {
        return ResponseEntity.status(error.getErrorCode().getStatus())
                .body(Response.error(error.getErrorCode().name()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> applicationHandler(MissingRequestHeaderException error) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.error(ErrorCode.HEADER_NOT_FOUND.name()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException error) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }
}
