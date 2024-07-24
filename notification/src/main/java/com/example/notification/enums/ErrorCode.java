package com.example.notification.enums;

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
    HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Header not found"),
    NOTIFICATION_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error while connecting notification");

    private HttpStatus status;
    private String message;
}