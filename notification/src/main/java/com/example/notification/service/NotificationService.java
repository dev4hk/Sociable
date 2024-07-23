package com.example.notification.service;

import com.example.notification.entity.Notification;
import com.example.notification.enums.ErrorCode;
import com.example.notification.exception.NotificationException;
import com.example.notification.model.NotificationArgs;
import com.example.notification.model.User;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.request.NotificationRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public Page<Notification> getNotificationsByUser(Pageable pageable, String token) {
        User user = getUser(token);
        return this.notificationRepository.findAllByUser(user, pageable);
    }


    public void createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setNotificationType(request.getType());
        notification.setArgs(new NotificationArgs(request.getSourceUser().getId(), request.getTargetUser().getId()));
        notification.setUser(request.getTargetUser());
        notificationRepository.save(notification);
    }

    private User getUser(String token) {
        try {
            return userService.getUserProfile(token).getBody();
        } catch (Exception e) {
            if (e instanceof FeignException && ((FeignException) e).status() == 404) {
                throw new NotificationException(ErrorCode.USER_NOT_FOUND);
            } else {
                throw new NotificationException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }
}
