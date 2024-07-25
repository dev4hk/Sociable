package com.example.post.request;

import com.example.post.enums.NotificationType;
import com.example.post.model.FileInfo;
import com.example.post.model.User;
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