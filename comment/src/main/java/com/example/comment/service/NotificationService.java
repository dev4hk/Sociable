package com.example.comment.service;

import com.example.comment.model.Notification;
import com.example.comment.request.NotificationRequest;
import com.example.comment.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE", url = "http://localhost:8086")
public interface NotificationService {

    @GetMapping("/api/v1/notifications")
    Response<Notification> createAndSendNotification(@RequestBody NotificationRequest request);

}