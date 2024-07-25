package com.example.user.request;

import com.example.user.entity.User;
import com.example.user.enums.NotificationType;
import com.example.user.model.FileInfo;
import com.example.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private UserResponse sourceUser;
    private Integer targetUserId;
    private NotificationType type;
    private Integer contentId;
    private FileInfo fileInfo;

    public NotificationRequest(UserResponse sourceUser, Integer targetUserId, NotificationType type) {
        this.sourceUser = sourceUser;
        this.targetUserId = targetUserId;
        this.type = type;
    }
}
