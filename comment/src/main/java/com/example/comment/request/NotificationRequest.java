package com.example.comment.request;

import com.example.comment.enums.NotificationType;
import com.example.comment.model.FileInfo;
import com.example.comment.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private User sourceUser;
    private Integer targetUserId;
    private NotificationType type;
    private Integer contentId;
}