package com.example.notification.service;

import com.example.notification.entity.Notification;
import com.example.notification.enums.ErrorCode;
import com.example.notification.enums.NotificationType;
import com.example.notification.exception.NotificationException;
import com.example.notification.fixture.UserFixture;
import com.example.notification.model.User;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.request.NotificationRequest;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    UserService userService;

    String token = "AABB";
    User user1 = UserFixture.getUser(1, "firstname1", "lastname1", "email1@email.com");
    User user2 = UserFixture.getUser(2, "firstname2", "lastname2", "email2@email.com");
    NotificationType type = NotificationType.NEW_COMMENT_ON_POST;
    NotificationRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new NotificationRequest(user1, user2, type);
    }

    @Test
    void get_notifications_by_user(){
        Pageable pageable = mock(Pageable.class);
        when(userService.getUserProfile(token)).thenReturn(ResponseEntity.of(Optional.of(this.user1)));
        when(notificationRepository.findAllByUser(user1, pageable)).thenReturn(Page.empty());
        assertDoesNotThrow(() -> notificationService.getNotificationsByUser(pageable, token));
    }

    @Test
    void get_notifications_by_non_existing_user_throws_exception() {
        Pageable pageable = mock(Pageable.class);
        Request request = Request.create(Request.HttpMethod.GET, "/api/v1/users/profile", new HashMap<>(), null, new RequestTemplate());
        when(userService.getUserProfile(token)).thenThrow(new FeignException.NotFound(null, request, null, null));
        NotificationException exception = assertThrows(NotificationException.class, () -> notificationService.getNotificationsByUser(pageable, token));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void create_notification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(any(Notification.class));
        assertDoesNotThrow(() -> notificationService.createAndSendNotification(request));
        verify(notificationRepository, times(1)).save(any());
    }
}