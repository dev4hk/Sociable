package com.example.user.model;

import com.example.user.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Long id;

    private NotificationType notificationType;

    private NotificationArgs args;

    private String notificationText;

    private Timestamp registeredAt;
}
