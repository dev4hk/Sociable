package com.example.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    NEW_COMMENT_ON_POST("New Comment!"),
    NEW_LIKE_ON_POST("New Like!"),
    FOLLOW_USER("User Followed!")
    ;

    private final String notificationText;
}