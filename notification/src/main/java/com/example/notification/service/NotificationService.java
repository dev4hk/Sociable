package com.example.notification.service;

import com.example.notification.entity.Notification;
import com.example.notification.enums.ErrorCode;
import com.example.notification.exception.NotificationException;
import com.example.notification.model.NotificationArgs;
import com.example.notification.model.User;
import com.example.notification.repository.EmitterRepository;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.request.NotificationRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final UserService userService;
    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String NOTIFICATION_NAME = "notification";

    public Page<Notification> getNotificationsByUser(Pageable pageable, String token) {
        User user = getUser(token);
        return this.notificationRepository.findAllByUserId(user.getId(), pageable);
    }


    @Transactional
    public Notification createAndSendNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setNotificationType(request.getType());
        notification.setArgs(new NotificationArgs(request.getSourceUser(), request.getTargetUserId(), request.getContentId()));
        Notification saved = notificationRepository.save(notification);
        this.send(saved.getId(), request.getTargetUserId());
        return saved;
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

    public SseEmitter connect(String token) {
        User user = getUser(token);
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(user.getId(), sseEmitter);
        sseEmitter.onCompletion(() -> emitterRepository.delete(user.getId()));
        sseEmitter.onTimeout(() -> emitterRepository.delete(user.getId()));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(NOTIFICATION_NAME).data("connect completed"));
        } catch (IOException exception) {
            throw new NotificationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
        }

        return sseEmitter;
    }

    public void send(Long notificationId, Integer targetUserId) {
        emitterRepository.get(targetUserId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(notificationId.toString()).name(NOTIFICATION_NAME).data("new notification"));
            } catch (IOException e) {
                emitterRepository.delete(targetUserId);
                throw new NotificationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }
}
