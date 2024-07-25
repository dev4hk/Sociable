package com.example.user.service;

import com.example.user.model.Notification;
import com.example.user.request.NotificationRequest;
import com.example.user.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE", url = "http://localhost:8086")
public interface NotificationService {

    @PostMapping("/api/v1/notifications")
    Response<Notification> createAndSendNotification(@RequestBody NotificationRequest request);

}
