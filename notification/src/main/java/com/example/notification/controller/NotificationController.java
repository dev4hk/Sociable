package com.example.notification.controller;

import com.example.notification.request.NotificationRequest;
import com.example.notification.response.NotificationResponse;
import com.example.notification.response.Response;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public Response<Void> createNotification(@RequestBody NotificationRequest request) {
        this.notificationService.createNotification(request);
        return Response.success();
    }

    @GetMapping
    public Response<Page<NotificationResponse>> getNotifications(Pageable pageable, @RequestHeader("Authorization") String token) {
        return Response.success(this.notificationService.getNotificationsByUser(pageable, token).map(NotificationResponse::fromNotification));
    }
}
