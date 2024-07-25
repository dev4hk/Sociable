package com.example.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    FOLLOW_USER("User Followed!");

    private final String notificationText;
}
