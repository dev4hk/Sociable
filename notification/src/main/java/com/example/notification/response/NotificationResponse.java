package com.example.notification.response;

import com.example.notification.entity.Notification;
import com.example.notification.enums.NotificationType;
import com.example.notification.model.NotificationArgs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    private NotificationType notificationType;

    private NotificationArgs args;

    private String notificationText;

    private Timestamp registeredAt;

    public static NotificationResponse fromNotification(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getNotificationType(),
                notification.getArgs(),
                notification.getNotificationType().getNotificationText(),
                notification.getRegisteredAt()
        );
    }
}
