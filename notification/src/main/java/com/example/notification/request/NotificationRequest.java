package com.example.notification.request;

import com.example.notification.enums.NotificationType;
import com.example.notification.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Integer sourceUserId;
    private Integer targetUserId;
    private NotificationType type;
    private Integer contentId;
}
