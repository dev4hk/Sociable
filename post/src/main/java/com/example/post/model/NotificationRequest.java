package com.example.post.model;

import com.example.post.enums.NotificationType;
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