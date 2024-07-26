package com.example.notification.controller;

import com.example.notification.request.NotificationRequest;
import com.example.notification.response.NotificationResponse;
import com.example.notification.response.Response;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public Response<NotificationResponse> createAndSendNotification(@RequestBody NotificationRequest request) {
        return Response.success(NotificationResponse.fromNotification(this.notificationService.createAndSendNotification(request)));
    }

    @GetMapping
    public Response<Page<NotificationResponse>> getNotifications(Pageable pageable, @RequestHeader("Authorization") String token) {
        return Response.success(this.notificationService.getNotificationsByUser(pageable, token).map(NotificationResponse::fromNotification));
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam("token") String token) {
        return this.notificationService.connect(token);
    }

    @DeleteMapping("/post")
    public Response<Void> deletePostNotifications(@RequestParam("postId") Integer postId) {
        this.notificationService.deletePostNotifications(postId);
        return Response.success();
    }
}
