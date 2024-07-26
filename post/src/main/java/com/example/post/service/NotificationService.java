package com.example.post.service;

import com.example.post.model.Notification;
import com.example.post.request.NotificationRequest;
import com.example.post.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NOTIFICATION-SERVICE", url = "http://localhost:8086")
public interface NotificationService {

    @PostMapping("/api/v1/notifications")
    Response<Notification> createAndSendNotification(@RequestBody NotificationRequest request);


    @DeleteMapping("/api/v1/notifications/post")
    Response<Void> deletePostNotifications(@RequestParam("postId") Integer postId);
}
