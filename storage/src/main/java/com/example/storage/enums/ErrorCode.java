package com.example.storage.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Request is invalid"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "File not found");

    private HttpStatus status;
    private String message;
}