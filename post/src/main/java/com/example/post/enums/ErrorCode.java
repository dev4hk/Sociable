package com.example.post.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Request is invalid"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    HEADER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Header is missing in request"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found");

    private HttpStatus status;
    private String message;
}
