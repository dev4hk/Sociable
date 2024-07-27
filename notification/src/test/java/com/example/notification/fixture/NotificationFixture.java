package com.example.notification.fixture;

import com.example.notification.entity.Notification;
import com.example.notification.enums.NotificationType;

import java.sql.Timestamp;

public class NotificationFixture {

    public static Notification get() {
        return new Notification(1L, null, NotificationType.NEW_COMMENT_ON_POST, null);
    }
}
